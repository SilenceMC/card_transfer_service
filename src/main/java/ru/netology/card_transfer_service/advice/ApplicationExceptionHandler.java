package ru.netology.card_transfer_service.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.card_transfer_service.dto.response.ClientErrorDTO;
import ru.netology.card_transfer_service.exception.CardValidateException;
import ru.netology.card_transfer_service.exception.OperationException;
import ru.netology.card_transfer_service.util.mapper.Status400ResponseDTOMapper;

import java.util.Objects;

public class ApplicationExceptionHandler {

    @ExceptionHandler({CardValidateException.class, OperationException.class})
    public ResponseEntity<ClientErrorDTO> cveHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(Status400ResponseDTOMapper.toDto(e.getMessage(), 400));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorDTO> manveHandler(BindException e) {
        return ResponseEntity
                .badRequest()
                .body(Status400ResponseDTOMapper
                        .toDto(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), 400));
    }

}
