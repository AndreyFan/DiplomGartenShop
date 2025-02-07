package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.OrderItemRepository;
import de.telran.gartenshop.repository.OrderRepository;
import de.telran.gartenshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.telran.gartenshop.entity.enums.OrderStatus.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
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

    //оформление заказа
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        //  if (userEntity != null) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        OrderEntity orderEntity = mappers.convertToOrderEntity(orderRequestDto);
        orderEntity.setUser(userEntity);
        userEntity.getOrderEntities().add(orderEntity);
        orderEntity.setContactPhone(userEntity.getPhone());
        orderEntity.setOrderStatus(OrderStatus.CREATED);
        orderEntity.setCreatedAt(timestamp);
        orderEntity.setUpdatedAt(timestamp);
        OrderEntity createdOrderEntity = orderRepository.save(orderEntity);
        orderItemRepository.saveAll(createdOrderEntity.getOrderItems());

        orderEntity.getOrderItems().forEach(item -> {
            item.setPriceAtPurchase(item.getProduct().getPrice());
            orderItemRepository.save(item);
        });
//        } else {
//            throw new NullPointerException("User with Id: " + userId + " not found.");
//        }
        return mappers.convertToOrderResponseDto(orderEntity);
    }

    // История покупок пользователя
    public Set<OrderResponseDto> getUsersOrders(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("This User not found ");
        } else {
            Set<OrderEntity> orderEntityList = user.getOrderEntities();
            // исключим из всех orderEntity юзера информацию о нем самом
            orderEntityList.forEach(order -> {
                order.setUser(null);
            } );
            return MapperUtil.convertSet(orderEntityList, mappers::convertToOrderResponseDto);
        }
    }

}
