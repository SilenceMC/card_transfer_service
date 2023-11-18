package ru.netology.card_transfer_service.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmOperationDTO {
    private UUID operationId;
    private String code;
}
