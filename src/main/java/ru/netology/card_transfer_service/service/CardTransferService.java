package ru.netology.card_transfer_service.service;

import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.dto.response.SuccessDTO;

import java.io.IOException;

public interface CardTransferService {

    boolean isRequestValid(TransferRequestDTO transferRequestDTO);

    boolean isTransferPossible(TransferRequestDTO transferRequestDTO);

    SuccessDTO createTransferOperation(TransferRequestDTO transferRequestDTO) throws IOException;

    SuccessDTO confirmTransferOperation(ConfirmOperationDTO confirmOperationDTO) throws IOException;
}
