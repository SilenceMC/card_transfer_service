package ru.netology.card_transfer_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.Accessors;
import ru.netology.card_transfer_service.domain.enums.Currency;
import ru.netology.card_transfer_service.exception.CardValidateException;


@Accessors(chain = true)
public record AmountDTO(
        @Min(value = 1, message = "Сумма перевода не может быть отрицательной или равной нулю")
        int value,

        @NotNull(message = "Код валюты (currency) не может быть null")
        @NotBlank(message = "Код валюты (currency) не может быть пустым")
        @Size(min = 3, max = 3, message = "Код валюты (currency) должен содержать 3 символа")
        Currency currency) {
}
