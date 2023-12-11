package ru.netology.card_transfer_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.SuccessDTO;

public interface CardTransferController {
    @PostMapping("/transfer")
    ResponseEntity<SuccessDTO> transfer(@RequestBody @Validated TransferRequestDTO transferRequestDto);

    @PostMapping("/confirmOperation")
    ResponseEntity<SuccessDTO> confirmTransferOperation(@RequestBody @Validated ConfirmOperationDTO confirmOperationDTO);
}
