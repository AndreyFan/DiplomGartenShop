package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.querydto.ProductAwaitingPaymentDto;
import de.telran.gartenshop.dto.querydto.ProductProfitDto;
import de.telran.gartenshop.dto.querydto.ProductTopPaidCanceledDto;
import de.telran.gartenshop.dto.requestdto.ProductRequestDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.CategoryRepository;
import de.telran.gartenshop.repository.ProductRepository;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // запускаем контейнер Spring для тестирования
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(SecurityConfig.class)
class ProductIntegrationTest {
    @Autowired
    private MockMvc mockMvc; // для имитации запросов пользователей

    @MockBean
    private ProductRepository productRepositoryMock;

    @MockBean
    private CategoryRepository categoryRepositoryMock;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntityTest;
    private CategoryEntity categoryEntityTest;

    private ProductRequestDto productRequestDtoTest;
    private ProductTopPaidCanceledDto productTopPaidCanceledDtoTest;
    private ProductAwaitingPaymentDto productAwaitingPaymentDtoTest;

    List<ProductEntity> productEntityList = new ArrayList<>();

    Timestamp timestamp;
    Long productIdTest;

    @BeforeEach
    void setUp() {
        productIdTest = 1L;
        timestamp = new Timestamp(new Date().getTime());

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

        ProductEntity productEntityTest2 = new ProductEntity(
                2L,
                "ProductName2",
                "ProductDescription2",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                categoryEntityTest);

        productEntityList.add(productEntityTest);
        productEntityList.add(productEntityTest2);

        productRequestDtoTest = new ProductRequestDto(
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                1L,
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"));

        productTopPaidCanceledDtoTest = new ProductTopPaidCanceledDto(
                1,
                "ProductName",
                5L,
                new BigDecimal("10.00"),
                new BigDecimal("100.00"));

        productAwaitingPaymentDtoTest = new ProductAwaitingPaymentDto(
                1,
                "ProductName",
                1,
                new Timestamp(2024-12-31),
                5);
    }

    @Test
    void getAllProductsTest() throws Exception {
        when(productRepositoryMock.findAll()).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..productId").value(1));
    }

    @Test
    void getProductByIdTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.of(productEntityTest));
        this.mockMvc.perform(get("/products/{productId}", productIdTest))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..productId").value(1));
    }

    @Test
    void getProductByIdExceptionByProductTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/products/{productId}", productIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductOfDayTest() throws Exception {
        when(productRepositoryMock.getProductOfDay()).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/productOfDay"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..productId").value(1));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void getProductOfDayListTest() throws Exception {
        when(productRepositoryMock.getProductOfDay()).thenReturn(productEntityList);
        this.mockMvc.perform(get("/products/productOfDay"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void deleteProductTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.of(productEntityTest));
        doNothing().when(productRepositoryMock).delete(productEntityTest);
        mockMvc.perform(delete("/products/{productId}", productIdTest))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void deleteProductExceptionByProductTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/products/{productId}", productIdTest))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void createProductTest() throws Exception {
        when(productRepositoryMock.save(any(ProductEntity.class))).thenReturn(productEntityTest);
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        this.mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void createProductReturnFalseTest() throws Exception {
        when(productRepositoryMock.save(any(ProductEntity.class))).thenReturn(new ProductEntity());
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        this.mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(content().string("false"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void updateProductTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.of(productEntityTest));
        when(productRepositoryMock.save(any(ProductEntity.class))).thenReturn(productEntityTest);
        this.mockMvc.perform(put("/products/{productId}", productIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("ProductName"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void updateProductExceptionByProductTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/products/{productId}", productIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void updateDiscountPriceTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.of(productEntityTest));
        when(productRepositoryMock.save(any(ProductEntity.class))).thenReturn(productEntityTest);
        this.mockMvc.perform(put("/products/discount/{productId}", productIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("ProductName"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void updateDiscountPriceExceptionByProductTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/products/discount/{productId}", productIdTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDtoTest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductsByFilterTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 9.99, 12.99,
                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?category=1&min_price=9.99&max_price=12.99&is_discount=true&sort=price,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByFilterWithoutCategoryTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 9.99, 12.99,
                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?min_price=9.99&max_price=12.99&is_discount=true&sort=price,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByFilterWithoutMinPriceTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 0.00, 12.99,
                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?max_price=12.99&is_discount=true&sort=price,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByFilterWithoutMaxPriceTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 9.99, 0.00,
                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?min_price=9.99&is_discount=true&sort=price,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByFilterWithoutMinMaxPriceTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, null, null,
                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?sort=price,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByFilterWithoutSortTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 9.99, 12.99,
                true, "price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?min_price=9.99&max_price=12.99&is_discount=true&sort=price,desc"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByFilterExceptionMinMaxPriceTest() throws Exception {
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 12.99, 9.99,
                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
        this.mockMvc.perform(get("/products/filter?min_price=12.99&max_price=9.99&is_discount=true&sort=price,desc"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void getProfitByPeriodTest() throws Exception {
        String periodTest = "WEEK";
        Integer valueTest = 5;

        ProductProfitDto productProfitDtoTest = new ProductProfitDto(
                "2025-01",
                new BigDecimal("100.00"));

        when(productRepositoryMock.getProductProfitByPeriod(anyString(), anyInt())).thenReturn(List.of(productProfitDtoTest));
        this.mockMvc.perform(get("/products/profit?period=week&value=5", periodTest, valueTest))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..period").exists())
                .andExpect(jsonPath("$..profit").exists())
                .andExpect(jsonPath("$..profit").value(100.00));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void getTop10PaidProductsTest() throws Exception {
        when(productRepositoryMock.getTop10PaidProducts()).thenReturn(List.of(productTopPaidCanceledDtoTest));
        this.mockMvc.perform(get("/products/top10paid"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..productId").value(1))
                .andExpect(jsonPath("$..productName").value("ProductName"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void getTopCanceledTest() throws Exception {
        when(productRepositoryMock.getTop10CanceledProducts()).thenReturn(List.of(productTopPaidCanceledDtoTest));
        this.mockMvc.perform(get("/products/top10canceled"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..productName").exists())
                .andExpect(jsonPath("$..productId").value(1))
                .andExpect(jsonPath("$..productName").value("ProductName"));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void getAwaitingPaymentProductsTest() throws Exception {
        when(productRepositoryMock.getAwaitingPaymentProducts(any(LocalDateTime.class)))
                .thenReturn(List.of(productAwaitingPaymentDtoTest));
        this.mockMvc.perform(get("/products/awaiting-payment-products")
                        .param("days", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..productId").value(1))
                .andExpect(jsonPath("$..productName").value("ProductName"));
    }

}
