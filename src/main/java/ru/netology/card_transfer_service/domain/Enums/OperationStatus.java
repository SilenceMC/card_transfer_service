package ru.netology.card_transfer_service.domain.Enums;

public enum OperationStatus {
    NEED_CONFIRM("Требует подтверждения"),
    CONFIRMED("Подтверждена"),
    DECLINED("Отклонена");

    private final String label;

    OperationStatus(String label) {
        this.label = label;
    }

    public String getlabel(){
        return label;
    }


}
