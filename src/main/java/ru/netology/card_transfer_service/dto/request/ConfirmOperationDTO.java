package ru.netology.card_transfer_service.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(chain = true)
public record ConfirmOperationDTO(UUID operationId, String code) {
}
