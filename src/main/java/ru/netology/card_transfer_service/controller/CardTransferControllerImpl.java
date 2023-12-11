package ru.netology.card_transfer_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.SuccessDTO;
import ru.netology.card_transfer_service.service.CardTransferServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CardTransferControllerImpl implements CardTransferController{
    private final CardTransferServiceImpl service;

    @SneakyThrows
    @PostMapping("/transfer")
    public ResponseEntity<SuccessDTO> transfer(@RequestBody @Validated TransferRequestDTO transferRequestDto) {
        return ResponseEntity.ok(service.createTransferOperation(transferRequestDto));
    }

    @SneakyThrows
    @PostMapping("/confirmOperation")
    public ResponseEntity<SuccessDTO> confirmTransferOperation(@RequestBody @Validated ConfirmOperationDTO confirmOperationDTO) {
        return ResponseEntity.ok(service.confirmTransferOperation(confirmOperationDTO));
    }
}
