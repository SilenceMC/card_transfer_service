package ru.netology.card_transfer_service.dto.response;

import lombok.Data;

@Data
public class Status400ResponseDTO {
    private String message;
    private int id;

    public Status400ResponseDTO(String message, int id) {
        this.message = message;
        this.id = id;
    }
}
