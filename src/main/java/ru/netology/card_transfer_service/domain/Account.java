package ru.netology.card_transfer_service.domain;

import lombok.Data;
import ru.netology.card_transfer_service.domain.Enums.Currency;

@Data
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