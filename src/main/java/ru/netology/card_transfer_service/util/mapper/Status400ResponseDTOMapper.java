package ru.netology.card_transfer_service.util.mapper;

import ru.netology.card_transfer_service.dto.response.Status400ResponseDTO;


public class Status400ResponseDTOMapper {
    public static Status400ResponseDTO toDto(String message, int id){
        return new Status400ResponseDTO(message, id);
    }
}
