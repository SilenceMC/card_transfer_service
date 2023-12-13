package ru.netology.card_transfer_service.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.netology.card_transfer_service.domain.enums.Currency;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Account {
    private String Number;
    private Currency currency;
    private int value;

    public Account(String number, Currency currency, int value) {
        Number = number;
        this.currency = currency;
        this.value = value;
    }
}