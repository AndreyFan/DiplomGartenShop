package de.telran.gartenshop.scheduler;

import de.telran.gartenshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class schedulerTest {
    @Mock
    private OrderService orderService;  // Мокируем OrderService

    @InjectMocks
    private ChangeOrderStatusScheduler scheduler;  // Внедряем мок в тестируемый класс

    @Test
    void changingOrderStatus_ShouldCallOrderServiceChangeStatus() {
        scheduler.changingOrderStatus();  // Запускаем вручную метод шедулера
        verify(orderService, times(1)).changeStatus();  // Проверяем, что метод вызвался ровно 1 раз
    }
}

