package ru.netology.card_transfer_service.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class TransferRequestDTO {
    @NotNull(message = "Номер карты (cardFromNumber) не может быть null")
    @NotBlank(message = "Номер карты (cardFromNumber) не может быть пустым")
    @Size(min = 16, max = 16, message = "Номер карты (cardFromNumber) должен содержать 16 символов")
    private String cardFromNumber;

    @NotNull(message = "Срок действия карты (cardFromValidTill) не может быть null")
    @NotBlank(message = "Срок действия карты (cardFromValidTill) не может быть пустым")
    private String cardFromValidTill;

    @NotNull(message = "CVV-код не может быть null")
    @NotBlank(message = "CVV-код не может быть пустым")
    @Size(min = 3, max = 3, message = "CVV-код должен содержать 3 символа")
    private String cardFromCVV;

    @NotNull(message = "Номер карты (cardToNumber) не может быть null")
    @NotBlank(message = "Номер карты (cardToNumber) не может быть пустым")
    @Size(min = 16, max = 16, message = "Номер карты (cardToNumber) должен содержать 16 символов")
    private String cardToNumber;
    private AmountDTO amount;
}
