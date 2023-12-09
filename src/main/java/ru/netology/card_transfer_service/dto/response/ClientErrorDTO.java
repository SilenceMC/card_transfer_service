package ru.netology.card_transfer_service.dto.response;

import lombok.Data;

@Data
public class ClientErrorDTO {
    private String message;
    private int id;

    public ClientErrorDTO(String message, int id) {
        this.message = message;
        this.id = id;
    }
}
