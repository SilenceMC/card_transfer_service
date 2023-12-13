package ru.netology.card_transfer_service.util.mapper;

import ru.netology.card_transfer_service.dto.response.ErrorDTO;


public class ClientErrorDTOMapper {
    public static ErrorDTO toDto(String message, int id) {
        return new ErrorDTO(message, id);
    }
}
