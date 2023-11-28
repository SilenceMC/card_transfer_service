package ru.netology.card_transfer_service.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ConfirmOperationDTO {
    private UUID operationId;
    private String code;
}
