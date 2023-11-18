package ru.netology.card_transfer_service.domain;

import lombok.Data;
import ru.netology.card_transfer_service.domain.Enums.Currency;
import ru.netology.card_transfer_service.domain.Enums.OperationStatus;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.repository.CardTransferRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Data
public class Operation {
    private UUID operationId;
    private OperationStatus status;
    private String confirmCode;
    private String cardFromNumber;
    private String cardToNumber;
    private int amount;
    private Currency currency;
    Random random = new Random();

    public Operation(TransferRequestDTO transferRequestDTO) {
        this.operationId = UUID.randomUUID();
        this.status = OperationStatus.NEED_CONFIRM;
        this.confirmCode = String.valueOf(random.nextInt(10000));
        this.cardFromNumber = transferRequestDTO.getCardFromNumber();
        this.cardToNumber = transferRequestDTO.getCardToNumber();
        this.amount = transferRequestDTO.getAmount().getValue();
        this.currency = transferRequestDTO.getAmount().getCurrency();

    }

    @Override
    public String toString() {
        return " Операция перевода " + operationId +
                " с карты " + cardFromNumber +
                " на карту " + cardToNumber +
                " в размере " + amount + currency +
                " в статусе: " + status.getlabel() + '\n';
    }
}
