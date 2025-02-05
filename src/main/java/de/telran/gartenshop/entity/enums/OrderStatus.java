package de.telran.gartenshop.entity.enums;

public enum OrderStatus {

    CREATED("Создан"),
    CANCELED("Отменено"),
    AWAIT_PAYMENT("Ожидает оплаты"),
    PAID("Оплачен"),
    ON_THE_WAY("В пути"),
    DELIVERED("Доставлено");

    private String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
