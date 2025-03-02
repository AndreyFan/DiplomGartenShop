package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.OrderRequestDto;
import de.telran.gartenshop.dto.responsedto.OrderItemResponseDto;
import de.telran.gartenshop.dto.responsedto.OrderResponseDto;
import de.telran.gartenshop.dto.responsedto.ProductResponseDto;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController implements OrderControllerInterface {

    private final OrderService orderService;

    @GetMapping("/status/{id}")
    public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable Long id) {
        return orderService.getOrderStatus(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/top-canceled")
    public ResponseEntity<List<ProductResponseDto>> getTopCanceled() {
        List<ProductResponseDto> products = orderService.getTop10CanceledProducts();
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/awaiting-payment-products")
    public ResponseEntity<List<ProductResponseDto>> getAwaitingPaymentProducts(@RequestParam(name = "days",
            defaultValue = "10") int days) {
        List<ProductResponseDto> products = orderService.getOrdersAwaitingPayment(days);
        return ResponseEntity.ok(products);
    }

    //Просмотр всех заказов //localhost:8088/orders/get
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
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

