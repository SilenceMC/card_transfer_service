package ru.netology.card_transfer_service.advice;

import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.card_transfer_service.dto.response.ErrorDTO;
import ru.netology.card_transfer_service.exception.CardValidateException;
import ru.netology.card_transfer_service.exception.OperationException;
import ru.netology.card_transfer_service.util.mapper.ClientErrorDTOMapper;

import java.io.IOException;
import java.util.Objects;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({CardValidateException.class, OperationException.class})
    public ResponseEntity<ErrorDTO> cveHandler(RuntimeException e) {
        return ResponseEntity.badRequest().body(ClientErrorDTOMapper.toDto(e.getMessage(), 400));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> manveHandler(BindException e) {
        return ResponseEntity
                .badRequest()
                .body(ClientErrorDTOMapper
                        .toDto(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), 400));
    }

//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<ErrorDTO> cveHandler(Error e) {
//        return ResponseEntity
//                .internalServerError()
//                .body(ClientErrorDTOMapper
//                        .toDto("Что-то пошло не так. Свяжитесь с администратором", 500));
//    }

}
