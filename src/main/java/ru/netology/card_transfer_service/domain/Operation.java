package ru.netology.card_transfer_service.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.netology.card_transfer_service.domain.enums.Currency;
import ru.netology.card_transfer_service.domain.enums.OperationStatus;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;

import java.util.Random;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
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
        this.confirmCode = "0000";
//        Для ФТ код всегда равен "0000". Но реализация изначально подразумевала случайное 4-значеное число
//        this.confirmCode = String.valueOf(random.nextInt(10000));
        this.cardFromNumber = transferRequestDTO.cardFromNumber();
        this.cardToNumber = transferRequestDTO.cardToNumber();
        this.amount = transferRequestDTO.amount().value();
        this.currency = transferRequestDTO.amount().currency();

    }

    @Override
    public String toString() {
        return " Операция перевода " + operationId +
                " с карты " + cardFromNumber +
                " на карту " + cardToNumber +
                " в размере " + amount + currency +
                " в статусе: " + status.getLabel() + '\n';
    }
}