package de.telran.gartenshop.enums;

import de.telran.gartenshop.entity.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

class OrderStatusTest {

    @Test
    void GetTitleTest() {
        assertEquals("Создан", OrderStatus.CREATED.getTitle(), "Создан");
        assertEquals("Отменено", OrderStatus.CANCELED.getTitle(), "Отменено");
        assertEquals("Ожидает оплаты", OrderStatus.AWAITING_PAYMENT.getTitle(), "Ожидает оплаты");
        assertEquals("Оплачен", OrderStatus.PAID.getTitle(), "Оплачен");
        assertEquals("В пути", OrderStatus.ON_THE_WAY.getTitle(), "В пути");
        assertEquals("Доставлено", OrderStatus.DELIVERED.getTitle(), "Доставлено");
    }
}
