package ru.netology.card_transfer_service.service;

import org.springframework.stereotype.Service;
import ru.netology.card_transfer_service.domain.Account;
import ru.netology.card_transfer_service.domain.Card;
import ru.netology.card_transfer_service.domain.Enums.OperationStatus;
import ru.netology.card_transfer_service.domain.Operation;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.Status200ResponseDTO;
import ru.netology.card_transfer_service.exception.CardValidateException;
import ru.netology.card_transfer_service.exception.OperationException;
import ru.netology.card_transfer_service.repository.CardTransferRepository;
import ru.netology.card_transfer_service.util.mapper.Status200ResponseDTOMapper;

import java.io.IOException;
import java.util.Optional;

@Service
public class CardTransferService {

    private final CardTransferRepository repository;

    public CardTransferService(CardTransferRepository repository) {
        this.repository = repository;
    }

    public boolean isRequestValid(TransferRequestDTO transferRequestDTO) {
        Optional<Card> cardFrom = repository.getCard(transferRequestDTO.getCardFromNumber());
        if (cardFrom.isEmpty())
            throw new CardValidateException("Карта списания не найдена");
        if (!cardFrom.get().getValidTill().equals(transferRequestDTO.getCardFromValidTill()))
            throw new CardValidateException("Срок действия карты некорректный");
        if (!cardFrom.get().getCvv().equals(transferRequestDTO.getCardFromCVV()))
            throw new CardValidateException("CVV-код некорректный");

        Account accountFrom = repository.getAccount(cardFrom.get().getAccountNumber());
        if (!accountFrom.getCurrency().equals(transferRequestDTO.getAmount().getCurrency()))
            throw new CardValidateException("Валюта счета списания не совпадает с валютой операции");

        Optional<Card> cardTo = repository.getCard(transferRequestDTO.getCardToNumber());
        if (cardTo.isEmpty())
            throw new CardValidateException("Карта зачисления не найдена");
        return true;
    }

    public boolean isTransferPossible(TransferRequestDTO transferRequestDTO) {
        String accountFrom = repository.getCard(transferRequestDTO.getCardFromNumber()).get().getAccountNumber();
        if (repository.getAccount(accountFrom).getValue() < transferRequestDTO.getAmount().getValue())
            throw new CardValidateException("Недостаточно средств на счете карты списания");
        return true;
    }


    public Status200ResponseDTO createTransferOperation(TransferRequestDTO transferRequestDTO) throws IOException {
        if (isRequestValid(transferRequestDTO) & isTransferPossible(transferRequestDTO)) {
            return Status200ResponseDTOMapper.toDto(repository.createTransferOperation(transferRequestDTO));
        }
        return null;
    }

    public synchronized Status200ResponseDTO confirmTransferOperation(ConfirmOperationDTO confirmOperationDTO) throws IOException {
        Optional<Operation> operation = repository.getOperation(confirmOperationDTO.getOperationId());
        if (operation.isEmpty())
            throw new OperationException("Нет операции перевода с указанным operationId");
        if (operation.get().getStatus().equals(OperationStatus.DECLINED))
            throw new OperationException("Операция с указанным operationId была отклонена");
        if (!operation.get().getConfirmCode().equals(confirmOperationDTO.getCode())) {
            repository.declineTransferOperation(confirmOperationDTO.getOperationId());
            throw new OperationException("Некорректный код подтверждения. Операция отклонена");
        }
        return Status200ResponseDTOMapper.toDto(repository.confirmTransferOperation(confirmOperationDTO));
    }
}
