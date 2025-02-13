package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.requestDto.OrderRequestDto;
import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.OrderResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.*;
import de.telran.gartenshop.entity.enums.DeliveryMethod;
import de.telran.gartenshop.entity.enums.OrderStatus;
import de.telran.gartenshop.repository.CategoryRepository;
import de.telran.gartenshop.repository.OrderItemRepository;
import de.telran.gartenshop.repository.OrderRepository;
import de.telran.gartenshop.repository.ProductRepository;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntityTest;
    private CategoryEntity categoryEntityTest;
    private OrderEntity orderEntityTest;
    private OrderItemEntity orderItemTest;

    private ProductRequestDto productRequestDtoTest;
    private CategoryRequestDto categoryRequestDtoTest;
    private OrderRequestDto orderRequesDtoTest;

    private ProductResponseDto productResponseDtoTest;
    private CategoryResponseDto categoryResponseDtoTest;

    Timestamp timestamp;

    Long orderIdTest;

    @BeforeEach
    void setUp() {
        orderIdTest = 1L;
        timestamp = new Timestamp(new Date().getTime());

        orderRequesDtoTest = new OrderRequestDto(
                "Berlin",
                DeliveryMethod.SELF_DELIVERY);

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

        categoryEntityTest = new CategoryEntity(1L, "CategoryName", null);
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

        orderItemTest = new OrderItemEntity(
                1L,
                100,
                new BigDecimal("8.50"),
                productEntityTest,
                orderEntityTest);
//
//        productRequestDtoTest = new ProductRequestDto(
//                "ProductName",
//                "ProductDescription",
//                new BigDecimal("10.25"),
//                1L,
//                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
//                new BigDecimal("8.50"));
//
//        categoryResponseDtoTest = new CategoryResponseDto(1L, "CategoryName");
//        productResponseDtoTest = new ProductResponseDto(
//                1L,
//                "ProductName",
//                "ProductDescription",
//                new BigDecimal("10.25"),
//                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
//                new BigDecimal("8.50"),
//                timestamp,
//                timestamp,
//                categoryResponseDtoTest);
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
        when(orderItemRepositoryMock.findAll()).thenReturn(List.of(orderItemTest));
        this.mockMvc.perform(get("/orders/get/items"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..orderItemId").exists())
                .andExpect(jsonPath("$..orderItemId").value(1));
    }
}
