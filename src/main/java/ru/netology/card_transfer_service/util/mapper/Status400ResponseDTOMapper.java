package ru.netology.card_transfer_service.util.mapper;

import ru.netology.card_transfer_service.dto.response.ClientErrorDTO;


public class Status400ResponseDTOMapper {
    public static ClientErrorDTO toDto(String message, int id) {
        return new ClientErrorDTO(message, id);
    }
}
