package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.OrderItemResponseDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.service.OrderService;
import jakarta.validation.constraints.Min;
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

    @GetMapping("/status/{id}")
    public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable Long id) {
        return orderService.getOrderStatus(id);
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<OrderResponseDto>> getTop10PaidProducts() {
        List<OrderResponseDto> products = orderService.getTop10PaidProducts();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/top-canceled")
    public ResponseEntity<List<ProductResponseDto>> getTopCanceled() {
        List<ProductResponseDto> products = orderService.getTop10CanceledProducts();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/awaiting-payment-products")
    public ResponseEntity<List<OrderResponseDto>> getAwaitingPaymentProducts(@RequestParam(name = "days",
            defaultValue = "10") int days) {
        List<OrderResponseDto> products = orderService.getOrdersAwaitingPayment(days);
        return ResponseEntity.ok(products);
    }


    //Просмотр всех заказов //localhost:8088/orders/get
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    //Просмотр товаров во всех заказах //localhost:8088/orders/get/items
    @GetMapping(value = "/get/items")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemResponseDto> getAllOrderItems() {
        return orderService.getAllOrderItems();
    }

    //Оформление заказа (поиск по userId), все товары из CartItems переходят в OrderItems //localhost:8088/orders/1
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, @PathVariable Long userId) {
        return orderService.createOrder(orderRequestDto, userId);
    }

    //История покупок пользователя
    // http://localhost:8088/orders/history/6
    @GetMapping("/history/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<OrderResponseDto> getUsersOrders(@PathVariable Long userId) {
        return orderService.getUsersOrders(userId);
    }

    //Отмена заказа по orderId //localhost:8088/orders/1
    @PutMapping(value = "/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}

