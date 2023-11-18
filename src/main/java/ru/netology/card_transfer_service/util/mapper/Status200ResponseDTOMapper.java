package ru.netology.card_transfer_service.util.mapper;

import ru.netology.card_transfer_service.dto.response.Status200ResponseDTO;

import java.util.UUID;

public class Status200ResponseDTOMapper {
    public static Status200ResponseDTO toDto(UUID operationId){
        return new Status200ResponseDTO(operationId);
    }
}
