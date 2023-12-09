package ru.netology.card_transfer_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.netology.card_transfer_service.domain.enums.Currency;

@Data
@Accessors(chain = true)
public class AmountDTO {

    @Min(value = 0, message = "Сумма перевода (value) не может быть отрицательной")
    private int value;

    @NotNull(message = "Код валюты (currency) не может быть null")
    @NotBlank(message = "Код валюты (currency) не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты (currency) должен содержать 3 символа")
    private Currency currency;

}
