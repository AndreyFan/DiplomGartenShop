package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.responseDto.AwaitingPaymentDto;
import de.telran.gartenshop.dto.responseDto.CanceledOrderDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.dto.responseDto.TopProductsDto;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<TopProductsDto>> getTopProducts() {
        return ResponseEntity.ok(orderService.getTop10Products());
    }

    @GetMapping("/top-canceled")
    public ResponseEntity <List<CanceledOrderDto>> getTopCanceled() {
        return ResponseEntity.ok(orderService.getTop10CanceledOrders());
    }

//    @GetMapping("/products-awaiting")
//    public ResponseEntity<List<AwaitingPaymentDto>> getProductsAwaiting(@RequestParam Long productId) {
//        return ResponseEntity.ok(orderService.getAwaitingPayment(productId));
//    }

    //просмотр всех заказов

    // /orders/get
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    //Оформление заказа
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public boolean createOrder(@RequestBody OrderRequestDto orderRequestDto) {
//        return orderService.createOrder(orderRequestDto);
//    }

}
