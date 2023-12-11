package ru.netology.card_transfer_service.dto.response;

import lombok.Data;

import java.util.UUID;

public record SuccessDTO(UUID operationId) {
}
