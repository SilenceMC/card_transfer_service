package ru.netology.card_transfer_service.repository;

import ru.netology.card_transfer_service.domain.Account;
import ru.netology.card_transfer_service.domain.Card;
import ru.netology.card_transfer_service.domain.Operation;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface CardTransferRepository {
    Optional<Card> getCard(String cardNumber);

    Account getAccount(String accountNumber);

    UUID createTransferOperation(TransferRequestDTO transferRequestDto) throws IOException;

    Optional<Operation> getOperation(UUID operationId);

    UUID confirmTransferOperation(ConfirmOperationDTO confirmOperationDTO) throws IOException;

    void declineTransferOperation(UUID operationId) throws IOException;

}
