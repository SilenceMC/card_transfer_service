package ru.netology.card_transfer_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.card_transfer_service.domain.Account;
import ru.netology.card_transfer_service.domain.Card;
import ru.netology.card_transfer_service.domain.enums.OperationStatus;
import ru.netology.card_transfer_service.domain.Operation;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.SuccessDTO;
import ru.netology.card_transfer_service.exception.CardValidateException;
import ru.netology.card_transfer_service.exception.OperationException;
import ru.netology.card_transfer_service.repository.CardTransferRepositoryImpl;
import ru.netology.card_transfer_service.util.mapper.SuccessDTOMapper;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardTransferServiceImpl implements CardTransferService{

    private final CardTransferRepositoryImpl repository;

    public boolean isRequestValid(TransferRequestDTO transferRequestDTO) {
        Optional<Card> cardFrom = repository.getCard(transferRequestDTO.cardFromNumber());
        if (cardFrom.isEmpty())
            throw new CardValidateException("Карта списания не найдена");
        if (!cardFrom.get().getValidTill().equals(transferRequestDTO.cardFromValidTill()))
            throw new CardValidateException("Срок действия карты некорректный");
        if (!cardFrom.get().getCvv().equals(transferRequestDTO.cardFromCVV()))
            throw new CardValidateException("CVV-код некорректный");

        Account accountFrom = repository.getAccount(cardFrom.get().getAccountNumber());
        if (!accountFrom.getCurrency().equals(transferRequestDTO.amount().currency()))
            throw new CardValidateException("Валюта счета списания не совпадает с валютой операции");

        Optional<Card> cardTo = repository.getCard(transferRequestDTO.cardToNumber());
        if (cardTo.isEmpty())
            throw new CardValidateException("Карта зачисления не найдена");
        return true;
    }

    public boolean isTransferPossible(TransferRequestDTO transferRequestDTO) {
        String accountFrom = repository.getCard(transferRequestDTO.cardFromNumber()).get().getAccountNumber();
        if (repository.getAccount(accountFrom).getValue() < transferRequestDTO.amount().value())
            throw new CardValidateException("Недостаточно средств на счете карты списания");
        return true;
    }


    public SuccessDTO createTransferOperation(TransferRequestDTO transferRequestDTO) throws IOException {
        if (isRequestValid(transferRequestDTO) & isTransferPossible(transferRequestDTO)) {
            return SuccessDTOMapper.toDto(repository.createTransferOperation(transferRequestDTO));
        }
        return null;
    }

    public synchronized SuccessDTO confirmTransferOperation(ConfirmOperationDTO confirmOperationDTO) throws IOException {
        Optional<Operation> operation = repository.getOperation(confirmOperationDTO.operationId());
        if (operation.isEmpty())
            throw new OperationException("Нет операции перевода с указанным operationId");
        if (operation.get().getStatus().equals(OperationStatus.DECLINED))
            throw new OperationException("Операция с указанным operationId была отклонена");
        if (!operation.get().getConfirmCode().equals(confirmOperationDTO.code())) {
            repository.declineTransferOperation(confirmOperationDTO.operationId());
            throw new OperationException("Некорректный код подтверждения. Операция отклонена");
        }
        return SuccessDTOMapper.toDto(repository.confirmTransferOperation(confirmOperationDTO));
    }
}
