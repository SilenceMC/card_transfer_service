package ru.netology.card_transfer_service.exception;

public class CardValidateException extends RuntimeException{
    public CardValidateException(String msg) {
        super(msg);
    }
}
