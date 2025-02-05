package de.telran.gartenshop.service;

import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public ResponseEntity<OrderStatus> getOrderStatus(Long orderId) {
       Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
       if (orderEntity.isPresent()) {
           return ResponseEntity.ok(orderEntity.get().getOrderStatus());
       }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
