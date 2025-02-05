package de.telran.gartenshop.controller;


import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping( "/Status")
    public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable Long orderId) {
        return orderService.getOrderStatus(orderId);
    }

}
