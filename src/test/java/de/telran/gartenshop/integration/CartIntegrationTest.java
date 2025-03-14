package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestdto.CartItemRequestDto;
import de.telran.gartenshop.entity.*;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.repository.*;
import de.telran.gartenshop.security.configure.SecurityConfig;
import de.telran.gartenshop.security.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // запускаем контейнер Spring для тестирования
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(SecurityConfig.class)
@WithMockUser(roles = {"CLIENT"})
class CartIntegrationTest {
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

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntityTest;
    private CartEntity cartEntityTest;
    private CartItemEntity cartItemEntityTest;
    private UserEntity userEntityTest;

    private CartItemRequestDto cartItemRequestDtoTest;

    Timestamp timestamp;
    Long userIdTest = 1L;
    Long cartItemIdTest = 1L;

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
                "refreshToken",
                cartEntityTest,
                null,
                null
        );

        cartItemRequestDtoTest = new CartItemRequestDto(
                1L,
                1);
    }

    @WithMockUser(roles = {"ADMINISTRATOR"})
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
    void getAllCartItemsByUserIdExceptionByUserTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/cart/get/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createCartItemTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        when(cartItemRepositoryMock.save(any(CartItemEntity.class))).thenReturn(cartItemEntityTest);
        when(productRepositoryMock.findById(cartItemEntityTest.getProduct().getProductId())).thenReturn(Optional.of(productEntityTest));
        this.mockMvc.perform(post("/cart/{userId}", userIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequestDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));
    }

    @Test
    void createCartItemReturnFalseTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        when(cartItemRepositoryMock.save(any(CartItemEntity.class))).thenReturn(new CartItemEntity());
        when(productRepositoryMock.findById(cartItemEntityTest.getProduct().getProductId())).thenReturn(Optional.of(productEntityTest));
        this.mockMvc.perform(post("/cart/{userId}", userIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequestDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(content().string("false"));
    }

    @Test
    void createCartItemExceptionByProductTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        when(cartItemRepositoryMock.save(any(CartItemEntity.class))).thenReturn(cartItemEntityTest);
        when(productRepositoryMock.findById(cartItemEntityTest.getProduct().getProductId())).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/cart/{userId}", userIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequestDtoTest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createCartItemExceptionByUserTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.empty());
        when(cartItemRepositoryMock.save(any(CartItemEntity.class))).thenReturn(cartItemEntityTest);
        when(productRepositoryMock.findById(cartItemEntityTest.getProduct().getProductId())).thenReturn(Optional.of(productEntityTest));

        this.mockMvc.perform(post("/cart/{userId}", userIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequestDtoTest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCartItemTest() throws Exception {
        when(cartItemRepositoryMock.findById(cartItemIdTest)).thenReturn(Optional.of(cartItemEntityTest));
        doNothing().when(cartItemRepositoryMock).delete(cartItemEntityTest);
        mockMvc.perform(delete("/cart/{cartItemId}", cartItemIdTest))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllCartItemsExceptionByCartItemTest() throws Exception {
        when(cartItemRepositoryMock.findById(cartItemIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/cart/{cartItemId}", cartItemIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAllCartItemsTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.of(userEntityTest));
        doNothing().when(cartItemRepositoryMock).delete(cartItemEntityTest);
        mockMvc.perform(delete("/cart/del/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllCartItemsExceptionByUserTest() throws Exception {
        when(userRepositoryMock.findById(userIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/cart/del/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
