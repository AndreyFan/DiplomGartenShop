package de.telran.gartenshop.service;

import de.telran.gartenshop.configure.MapperUtil;
import de.telran.gartenshop.dto.requestdto.OrderRequestDto;

import de.telran.gartenshop.dto.responsedto.*;
import de.telran.gartenshop.entity.*;

import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.exception.DataNotFoundInDataBaseException;
import de.telran.gartenshop.exception.UserNotFoundException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import java.util.*;

import static de.telran.gartenshop.entity.enums.OrderStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Разрешает повторные запросы
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final Mappers mappers;

    OrderStatus nextStatus;

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
            nextStatus = switch (currentStatus) {
                case PAID -> ON_THE_WAY;
                case ON_THE_WAY -> DELIVERED;
                default -> currentStatus;
            };
        });
        // Save everything with one request
        orderRepository.saveAll(orderEntityList);
    }

    //Оформление заказа
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long userId) {

        //1. Создание заказа (Orders)
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        OrderEntity orderEntity = mappers.convertToOrderEntity(orderRequestDto);
        OrderEntity createOrderEntity;
        if (userEntity != null) {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            orderEntity.setUser(userEntity);
            userEntity.getOrderEntities().add(orderEntity);
            orderEntity.setContactPhone(userEntity.getPhone());
            orderEntity.setOrderStatus(OrderStatus.CREATED);
            orderEntity.setCreatedAt(timestamp);
            orderEntity.setUpdatedAt(timestamp);
            createOrderEntity = orderRepository.save(orderEntity);
        } else {
            throw new UserNotFoundException("User not found with Id: " + userId);
        }

        //2. Заполнение товаров в заказе (преобразование CartItems в OrderItems)
        createOrderItems(createOrderEntity, userEntity);

        return mappers.convertToOrderResponseDto(orderEntity);
    }

    //2. Заполнение товаров в заказе (преобразование CartItems в OrderItems)
    public void createOrderItems(OrderEntity createOrderEntity, UserEntity userEntity) {

        CartEntity cartEntity = userEntity.getCart();
        if (cartEntity != null) {
            Set<CartItemEntity> cartItemEntitySet = userEntity.getCart().getCartItems();
            Set<OrderItemEntity> orderItemEntitySet = new HashSet<>();
            for (CartItemEntity orderItem : cartItemEntitySet) {
                OrderItemEntity createOrderItem = new OrderItemEntity();
                ProductEntity productEntity = productRepository.findById(orderItem.getProduct().getProductId()).orElse(null);
                if (productEntity != null) {
                    createOrderItem.setProduct(productEntity);
                    if (productEntity.getDiscountPrice() == null) {
                        createOrderItem.setPriceAtPurchase(productEntity.getPrice());
                    } else {
                        createOrderItem.setPriceAtPurchase(productEntity.getDiscountPrice());
                    }
                    createOrderItem.setQuantity(orderItem.getQuantity());
                    createOrderItem.setOrder(createOrderEntity);
                    orderItemRepository.save(createOrderItem);

                    orderItemEntitySet.add(createOrderItem);
                } else {
                    throw new DataNotFoundInDataBaseException("Product " + orderItem.getProduct().getName() + " not found.");
                }

                createOrderEntity.setOrderItems(orderItemEntitySet);
                orderRepository.save(createOrderEntity);

                //3. Очищение товаров в корзине (удаление CartItems)
                deleteCartItems(cartEntity);
            }
        } else {
            throw new DataNotFoundInDataBaseException("Cart for UserId: " + userEntity.getUserId() + " not found.");
        }
    }

    //3. Очищение товаров в корзине (удаление CartItems)
    public void deleteCartItems(CartEntity cartEntity) {
        Set<CartItemEntity> cartItemSet = cartEntity.getCartItems();
        for (CartItemEntity item : cartItemSet) {
            cartItemRepository.delete(item);
        }
    }

    // История покупок пользователя
    public Set<OrderResponseDto> getUsersOrders(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("This User not found ");
        } else {
            Set<OrderEntity> orderEntityList = user.getOrderEntities();
            // исключим из всех orderEntity юзера информацию о нем самом
            orderEntityList.forEach(order -> order.setUser(null));
            return MapperUtil.convertSet(orderEntityList, mappers::convertToOrderResponseDto);
        }
    }

    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        OrderEntity updateOrderEntity;
        if (orderEntity != null) {
            if (orderEntity.getOrderStatus() == OrderStatus.CREATED || orderEntity.getOrderStatus() == OrderStatus.AWAITING_PAYMENT) {
                Timestamp timestamp = new Timestamp(new Date().getTime());
                orderEntity.setOrderStatus(OrderStatus.CANCELED);
                orderEntity.setUpdatedAt(timestamp);
                updateOrderEntity = orderRepository.save(orderEntity);
            } else {
                throw new DataNotFoundInDataBaseException("Order with Id: " + +orderId + " could not be cancel");
            }
        } else {
            throw new DataNotFoundInDataBaseException("Order not found with Id: " + orderId);
        }
        return mappers.convertToOrderResponseDto(updateOrderEntity);
    }
}
