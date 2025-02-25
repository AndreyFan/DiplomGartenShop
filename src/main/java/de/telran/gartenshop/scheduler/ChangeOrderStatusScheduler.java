package de.telran.gartenshop.scheduler;

import de.telran.gartenshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeOrderStatusScheduler {

    private final OrderService orderService;

    @Scheduled(initialDelayString = "${initialDelay}", fixedRateString ="${fixedRate}")
    public void changingOrderStatus() {
        log.info("Запускается изменение статуса заказа");
        orderService.changeStatus();
    }
}
