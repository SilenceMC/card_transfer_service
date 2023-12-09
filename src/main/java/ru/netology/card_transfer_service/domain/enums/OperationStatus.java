package ru.netology.card_transfer_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OperationStatus {
    NEED_CONFIRM("Требует подтверждения"),
    CONFIRMED("Подтверждена"),
    DECLINED("Отклонена");

    @Getter
    private final String label;
}
