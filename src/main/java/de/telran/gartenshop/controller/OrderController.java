package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.ProductEntity;
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
    public ResponseEntity<List<OrderEntity>> getTop10PaidProducts() {
        return ResponseEntity.ok(orderService.getTop10PaidProducts());
    }

    @GetMapping("/top-canceled")
    public ResponseEntity <List<ProductEntity>> getTopCanceled() {
        return ResponseEntity.ok(orderService.getTop10CanceledProducts());
    }

    @GetMapping("/awaiting-payment-products")
    public ResponseEntity<List<OrderEntity>> getAwaitingPaymentProducts(@RequestParam(name = "days",
            defaultValue = "10") int days) {
        List<OrderEntity> products = orderService.getOrdersAwaitingPayment(days);
        return ResponseEntity.ok(products);
    }



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
