package ru.netology.card_transfer_service.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class SuccessDTO {
    private UUID operationId;

    public SuccessDTO(UUID operationId) {
        this.operationId = operationId;
    }
}
