package ru.netology.card_transfer_service.repository;

import org.springframework.stereotype.Repository;
import ru.netology.card_transfer_service.domain.Account;
import ru.netology.card_transfer_service.domain.Card;
import ru.netology.card_transfer_service.domain.Enums.Currency;
import ru.netology.card_transfer_service.domain.Enums.OperationStatus;
import ru.netology.card_transfer_service.domain.Operation;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.util.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CardTransferRepository {
    private Map<String, Card> cards = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();

    private Map<UUID, Operation> operations = new HashMap<>();

    public CardTransferRepository() {
        cards.put("1234123412341234", new Card("1234123412341234", "12/24", "123", "1"));
        cards.put("4321432143214321", new Card("4321432143214321", "12/24", "321", "2"));
        accounts.put("1", new Account("1", Currency.RUR, 1000));
        accounts.put("2", new Account("2", Currency.RUR, 0));
    }

    public Optional<Card> getCard(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public UUID createTransferOperation(TransferRequestDTO transferRequestDto) throws IOException {
        Operation operation = new Operation(transferRequestDto);
        operations.put(operation.getOperationId(), operation);
        Logger.log(String.valueOf(operation.toString()));
        return operation.getOperationId();
    }

    public Optional<Operation> getOperation(UUID operationId) {
        return Optional.ofNullable(operations.get(operationId));
    }

    public UUID confirmTransferOperation(ConfirmOperationDTO confirmOperationDTO) {
        Operation updateOperation = operations.get(confirmOperationDTO.getOperationId());
        updateOperation.setStatus(OperationStatus.CONFIRMED);

        Account accountFrom = accounts.get(cards.get(updateOperation.getCardFromNumber()).getAccountNumber());
        Account accountTo = accounts.get(cards.get(updateOperation.getCardToNumber()).getAccountNumber());

        accountFrom.setValue(accountFrom.getValue() - updateOperation.getAmount());
        accountTo.setValue(accountTo.getValue() + updateOperation.getAmount());

        operations.put(updateOperation.getOperationId(), updateOperation);
        return updateOperation.getOperationId();
    }

    public void declineTransferOperation(UUID operationId) throws IOException {
        Operation updateOperation = operations.get(operationId);
        updateOperation.setStatus(OperationStatus.DECLINED);
        operations.put(updateOperation.getOperationId(), updateOperation);
        Logger.log(updateOperation.toString());
    }
}
