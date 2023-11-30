package ru.netology.card_transfer_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.Status400ResponseDTO;
import ru.netology.card_transfer_service.dto.response.Status200ResponseDTO;
import ru.netology.card_transfer_service.exception.CardValidateException;
import ru.netology.card_transfer_service.exception.OperationException;
import ru.netology.card_transfer_service.service.CardTransferService;
import ru.netology.card_transfer_service.util.mapper.Status400ResponseDTOMapper;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class CardTransferController {
    private final CardTransferService service;

    public CardTransferController(CardTransferService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public Status200ResponseDTO transfer(@RequestBody @Validated TransferRequestDTO transferRequestDto) throws IOException {
        return service.createTransferOperation(transferRequestDto);
    }

    @PostMapping("/confirmOperation")
    public Status200ResponseDTO confirmTransferOperation(@RequestBody @Validated ConfirmOperationDTO confirmOperationDTO) throws IOException {
        return service.confirmTransferOperation(confirmOperationDTO);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CardValidateException.class, OperationException.class})
    public Status400ResponseDTO cveHandler(RuntimeException e) {
        return Status400ResponseDTOMapper.toDto(e.getMessage(), 400);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Status400ResponseDTO manveHandler(BindException e) {
        return Status400ResponseDTOMapper.toDto(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), 400);
    }
}
