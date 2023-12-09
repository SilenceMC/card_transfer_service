package ru.netology.card_transfer_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.SuccessDTO;
import ru.netology.card_transfer_service.service.CardTransferService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CardTransferController {
    private final CardTransferService service;

    @PostMapping("/transfer")
    public ResponseEntity<SuccessDTO> transfer(@RequestBody @Validated TransferRequestDTO transferRequestDto) throws IOException {
        return ResponseEntity.ok(service.createTransferOperation(transferRequestDto));
    }

    @PostMapping("/confirmOperation")
    public SuccessDTO confirmTransferOperation(@RequestBody @Validated ConfirmOperationDTO confirmOperationDTO) throws IOException {
        return service.confirmTransferOperation(confirmOperationDTO);
    }
}
