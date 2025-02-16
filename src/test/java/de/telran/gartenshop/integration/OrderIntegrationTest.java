package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.responseDto.*;
import de.telran.gartenshop.entity.*;
import de.telran.gartenshop.entity.enums.DeliveryMethod;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // запускаем контейнер Spring для тестирования
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles(profiles = {"dev"})
public class OrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc; // для имитации запросов пользователей

    @MockBean
    private ProductRepository productRepositoryMock;

    @MockBean
    private OrderRepository orderRepositoryMock;

    @MockBean
    private OrderItemRepository orderItemRepositoryMock;

    @MockBean
    private CategoryRepository categoryRepositoryMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntityTest;
    private OrderEntity orderEntityTest;
    private OrderItemEntity orderItemEntityTest;
    private UserEntity userEntityTest;

    private OrderRequestDto orderRequesDtoTest;

    Timestamp timestamp;
    Long orderIdTest = 1L;
    Long userIdTest = 1L;

    @BeforeEach
    void setUp() {
        timestamp = new Timestamp(new Date().getTime());

        orderRequesDtoTest = new OrderRequestDto(
                "Berlin",
                DeliveryMethod.SELF_DELIVERY);

        OrderResponseDto orderResponseDtoTest = new OrderResponseDto(
                1L,
                timestamp,
                "Berlin",
                "+49049544663",
                DeliveryMethod.SELF_DELIVERY,
                OrderStatus.CREATED,
                timestamp,
                new HashSet<OrderItemResponseDto>(),
                new UserResponseDto());

        orderEntityTest = new OrderEntity(
                1L,
                timestamp,
                "Berlin",
                "+49049544663",
                DeliveryMethod.SELF_DELIVERY,
                OrderStatus.CREATED,
                timestamp,
                new UserEntity(),
                new HashSet<OrderItemEntity>());

        CategoryEntity categoryEntityTest = new CategoryEntity(1L, "CategoryName", null);
        productEntityTest = new ProductEntity(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                categoryEntityTest);

        CategoryResponseDto categoryResponseDtoTest = new CategoryResponseDto(1L, "CategoryName");
        ProductResponseDto productResponseDtoTest = new ProductResponseDto(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                categoryResponseDtoTest);

        orderItemEntityTest = new OrderItemEntity(
                1L,
                100,
                new BigDecimal("8.50"),
                productEntityTest,
                orderEntityTest);

        OrderItemResponseDto orderItemResponseDtoTest = new OrderItemResponseDto(
                1L,
                100,
                new BigDecimal("8.50"),
                productResponseDtoTest);


        Set<OrderEntity> orderEntitySet = new HashSet<>();
        orderEntitySet.add(orderEntityTest);

        userEntityTest = new UserEntity(
                1L,
                "Tom Smith",
                "ts@gmail.com",
                "+4975644333",
                "hgfgjfdlgjflg",
                Role.CLIENT,
                new CartEntity(),
                new HashSet<FavoriteEntity>(),
                orderEntitySet);
    }

    @Test
    void getAllOrdersTest() throws Exception {
        when(orderRepositoryMock.findAll()).thenReturn(List.of(orderEntityTest));
        this.mockMvc.perform(get("/orders/get"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderId").exists())
                .andExpect(jsonPath("$..orderId").value(1));
    }

    @Test
    void getOrderStatusTest() throws Exception {
        when(orderRepositoryMock.findById(orderIdTest)).thenReturn(Optional.of(orderEntityTest));
        this.mockMvc.perform(get("/orders/status/{orderId}", orderIdTest))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk());
    }


    @Test
    void getOrderStatusExceptionByUserTest() throws Exception {
        when(orderRepositoryMock.findById(orderIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/orders/status/{orderId}", orderIdTest))
                .andDo(print())  // печать лога вызова
                .andExpect(status().isNotFound()); // ожидаем 404 статус
    }


    @Test
    void getTop10PaidProductsTest() throws Exception {
        when(orderRepositoryMock.findTop10PaidOrders()).thenReturn(List.of(orderEntityTest));
        this.mockMvc.perform(get("/orders/top-products"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderId").exists())
                .andExpect(jsonPath("$..orderId").value(1));
    }

    @Test
    void getTopCanceledTest() throws Exception {
        when(orderRepositoryMock.findTop10CanceledProducts()).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/orders/top-canceled"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..productId").value(1));
    }

    @Test
    void getAwaitingPaymentProductsTest() throws Exception {
        when(orderRepositoryMock.findOrdersAwaitingPayment(10)).thenReturn(List.of(orderEntityTest));
        this.mockMvc.perform(get("/orders/awaiting-payment-products"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderId").exists())
                .andExpect(jsonPath("$..orderId").value(1));
    }

    @Test
    void getAllOrderItemsTest() throws Exception {
        when(orderItemRepositoryMock.findAll()).thenReturn(List.of(orderItemEntityTest));
        this.mockMvc.perform(get("/orders/get/items"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderItemId").exists())
                .andExpect(jsonPath("$..orderItemId").value(1));
    }

    @Test
    void createOrderTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        when(orderRepositoryMock.save(any(OrderEntity.class))).thenReturn(orderEntityTest);
        when(orderItemRepositoryMock.save(any(OrderItemEntity.class))).thenReturn(orderItemEntityTest);
        when(productRepositoryMock.findById(orderItemEntityTest.getProduct().getProductId())).thenReturn(Optional.of(productEntityTest));
        this.mockMvc.perform(post("/orders/{userId}", userIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequesDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createOrderExceptionByUserTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(post("/orders/{userId}", userIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequesDtoTest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

//    @Test
//    void createOrderExceptionByProductTest() throws Exception {
//        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
//        when(orderRepositoryMock.save(any(OrderEntity.class))).thenReturn(orderEntityTest);
//        when(orderItemRepositoryMock.save(any(OrderItemEntity.class))).thenReturn(orderItemEntityTest);
//        when(productRepositoryMock.findById(orderItemEntityTest.getProduct().getProductId())).thenReturn(Optional.empty());
//        this.mockMvc.perform(post("/orders/{userId}", userIdTest)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(orderRequesDtoTest)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void cancelOrderTest() throws Exception {
        when(orderRepositoryMock.findById(orderIdTest)).thenReturn(Optional.of(orderEntityTest));
        when(orderRepositoryMock.save(any(OrderEntity.class))).thenReturn(orderEntityTest);
        this.mockMvc.perform(put("/orders/{orderId}", orderIdTest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderId").exists())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("CANCELED"));
    }

    @Test
    void cancelOrderExceptionByOrderTest() throws Exception {
        when(orderRepositoryMock.findById(orderIdTest)).thenReturn(Optional.empty());
        when(orderRepositoryMock.save(any(OrderEntity.class))).thenReturn(orderEntityTest);
        this.mockMvc.perform(put("/orders/{orderId}", orderIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelOrderExceptionByStatusOrderTest() throws Exception {
        when(orderRepositoryMock.findById(orderIdTest)).thenReturn(Optional.of(orderEntityTest));
        orderEntityTest.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepositoryMock.save(any(OrderEntity.class))).thenReturn(orderEntityTest);
        this.mockMvc.perform(put("/orders/{orderId}", orderIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersOrdersTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        this.mockMvc.perform(get("/orders/history/{userId}", userIdTest))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderId").exists())
                .andExpect(jsonPath("$..orderId").value(1));
    }

    @Test
    void getUsersOrdersExceptionByUserTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/orders/history/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
