package ru.netology.card_transfer_service.util.mapper;

import ru.netology.card_transfer_service.dto.response.SuccessDTO;

import java.util.UUID;

public class SuccessDTOMapper {
    public static SuccessDTO toDto(UUID operationId) {
        return new SuccessDTO(operationId);
    }
}
