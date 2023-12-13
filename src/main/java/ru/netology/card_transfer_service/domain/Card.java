package ru.netology.card_transfer_service.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Card {
    private String number;
    private String validTill;
    private String cvv;
    private String accountNumber;

    public Card(String number, String validTill, String cvv, String accountNumber) {
        this.number = number;
        this.validTill = validTill;
        this.cvv = cvv;
        this.accountNumber = accountNumber;
    }
}