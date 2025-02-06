package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.*;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.telran.gartenshop.entity.enums.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final Mappers mappers;

    public ResponseEntity<OrderStatus> getOrderStatus(Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isPresent()) {
            return ResponseEntity.ok(orderEntity.get().getOrderStatus());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public List<TopProductsDto> getTop10Products() {
        List<Object[]> results = orderRepository.findTop10Products();
        return results.stream()
                .map(obj -> new TopProductsDto((ProductEntity) obj[0], ((Number) obj[1]).intValue()))
                .collect(Collectors.toList());
    }

    public List<CanceledOrderDto> getTop10CanceledOrders() {
        List<Object[]> results = orderRepository.findTop10CanceledOrders();
        return results.stream()
                .map(obj -> new CanceledOrderDto(
                        ((OrderEntity) obj[0]).getOrderId(),
                        ((OrderEntity) obj[0]).getDeliveryAddress(),
                        ((OrderEntity) obj[0]).getCreatedAt(),
                        ((Number) obj[1]).intValue()
                ))
                .collect(Collectors.toList());
    }

//    public List<AwaitingPaymentDto> getAwaitingPayment(Long days) {
//        List<Object[]> results = orderRepository.findProductsAwaitingPaymentForMoreThanNDays(Math.toIntExact(days));
//        return results.stream()
//                .map(obj -> new AwaitingPaymentDto(
//                        (ProductEntity) obj[0],  // Продукт
//                        ((Number) obj[1]).intValue()  // Общее количество товаров
//                ))
//                .collect(Collectors.toList());
//    }

    public List<OrderResponseDto> getAllOrders() {
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        return MapperUtil.convertList(orderEntityList, mappers::convertToOrderResponseDto);
    }

    // changing orderStatus ( scheduler: period - 30 s )
    @Transactional
    public void changeStatus() {
        List<OrderEntity> orderEntityList = orderRepository.findAll();

        // to avoid ConcurrentModificationException,
        // first change all statuses and only then save as a list
        orderEntityList.forEach(order -> {
            OrderStatus currentStatus = order.getOrderStatus();
            OrderStatus nextStatus = switch (currentStatus) {
                case CREATED -> AWAITING_PAYMENT;
                case AWAITING_PAYMENT -> PAID;
                case PAID -> ON_THE_WAY;
                case ON_THE_WAY -> DELIVERED;
                default -> currentStatus;
            };
            order.setOrderStatus(nextStatus);
        });
        // Save everything with one request
        orderRepository.saveAll(orderEntityList);
    }

//    public boolean createOrder(OrderRequestDto orderRequestDto) {
//
//    }

    //получение
//    public OrderResponseDto getOrderById(Long orderId, String email) {
//        User user = userRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            throw new DataNotFoundInDataBaseException("User not found in database.");
//        }
//        Set<Order> ordersSet = user.getOrders();
//        for (Order order : ordersSet) {
//            if (order.getOrderId().equals(orderId)) {
//                OrderResponseDto orderResponseDto = mappers.convertToOrderResponseDto(order);
//                Set<OrderItemResponseDto> orderItemResponseDto = MapperUtil.convertSet(order.getOrderItems(), mappers::convertToOrderItemResponseDto);
//                orderResponseDto.setOrderItemsSet(orderItemResponseDto);
//                return orderResponseDto;
//            }
//        }
//
//        throw new DataNotFoundInDataBaseException("Order not found in database or doesn't belong to user.");
//    }
}
