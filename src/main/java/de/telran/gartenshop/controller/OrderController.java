package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/Status")
    public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable Long orderId) {
        return orderService.getOrderStatus(orderId);
    }

    //просмотр всех заказов

    // /orders/get
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    //Оформление заказа
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, @PathVariable Long userId) {
        return orderService.createOrder(orderRequestDto, userId);
    }

    //История покупок пользователя
    // http://localhost:8088/orders/history/6
    @GetMapping("/history/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<OrderResponseDto> getUsersOrders(@PathVariable Long userId){
        return orderService.getUsersOrders(userId);
    }

}
