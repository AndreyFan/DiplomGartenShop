package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public List<OrderResponseDto> getAllOrders() {
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        return MapperUtil.convertList(orderEntityList, mappers:: convertToOrderResponseDto);
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
