package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestDto.CartRequestDto;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // запускаем контейнер Spring для тестирования
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles(profiles = {"dev"})
public class CartIntegrationTest {
    @Autowired
    private MockMvc mockMvc; // для имитации запросов пользователей

    @MockBean
    private ProductRepository productRepositoryMock;

    @MockBean
    private CartRepository cartRepositoryMock;

    @MockBean
    private CartItemRepository cartItemRepositoryMock;

    @MockBean
    private CategoryRepository categoryRepositoryMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntityTest;
    private CartEntity cartEntityTest;
    private CartItemEntity cartItemEntityTest;
    private UserEntity userEntityTest;

    Timestamp timestamp;
    Long cartIdTest = 1L;
    Long userIdTest = 1L;

    @BeforeEach
    void setUp() {
        timestamp = new Timestamp(new Date().getTime());

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

        cartItemEntityTest = new CartItemEntity(
                1L,
                100,
                null,
                productEntityTest);

        Set<CartItemEntity> cartItemEntitySet = new HashSet<>();
        cartItemEntitySet.add(cartItemEntityTest);

        cartEntityTest = new CartEntity(
                1L,
                userEntityTest,
                cartItemEntitySet);

        cartItemEntityTest.setCart(cartEntityTest);

        userEntityTest = new UserEntity(
                1L,
                "Tom Smith",
                "ts@gmail.com",
                "+4975644333",
                "hgfgjfdlgjflg",
                Role.CLIENT,
                cartEntityTest,
                null,
                null);

//        CartItemResponseDto cartItemResponseDtoTest = new CartItemResponseDto(
//                1L,
//                100,
//                null,
//                productResponseDtoTest);
   }

    @Test
    void getAllCartItemsTest() throws Exception {
        when(cartItemRepositoryMock.findAll()).thenReturn(List.of(cartItemEntityTest));
        this.mockMvc.perform(get("/cart/get"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..cartItemId").exists())
                .andExpect(jsonPath("$..cartItemId").value(1));
    }

    @Test
    void getAllCartItemsByUserIdTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        this.mockMvc.perform(get("/cart/get/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..cartItemId").exists())
                .andExpect(jsonPath("$..cartItemId").value(1));
    }

    @Test
    void getAllCartItemsByUserIdByUserTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/cart/get/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
