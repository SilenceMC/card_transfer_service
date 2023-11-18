package ru.netology.card_transfer_service.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class Status200ResponseDTO {
    private UUID operationId;

    public Status200ResponseDTO(UUID operationId) {
        this.operationId = operationId;
    }
}
