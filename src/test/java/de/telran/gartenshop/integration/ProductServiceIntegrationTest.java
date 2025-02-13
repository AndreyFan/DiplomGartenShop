package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.requestDto.ProductRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.CategoryRepository;
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
public class ProductServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc; // для имитации запросов пользователей

    @MockBean
    private ProductRepository productRepositoryMock;

    @MockBean
    private CategoryRepository categoryRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntityTest;
    private CategoryEntity categoryEntityTest;

    private ProductRequestDto productRequestDtoTest;
    private CategoryRequestDto categoryRequestDtoTest;

    private ProductResponseDto productResponseDtoTest;
    private CategoryResponseDto categoryResponseDtoTest;

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

        productRequestDtoTest = new ProductRequestDto(
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                1L,
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"));

        categoryResponseDtoTest = new CategoryResponseDto(1L, "CategoryName");
        productResponseDtoTest = new ProductResponseDto(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                categoryResponseDtoTest);
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
    void deleteProductTest() throws Exception {
        when(productRepositoryMock.findById(productIdTest)).thenReturn(Optional.of(productEntityTest));
        doNothing().when(productRepositoryMock).delete(productEntityTest);
        mockMvc.perform(delete("/products/{productId}", productIdTest))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    void getProductsByFilterTest() throws Exception {
//        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(categoryEntityTest));
//        when(productRepositoryMock.findProductByFilter(categoryEntityTest, 9.99, 12.99,
//                true, "sort=price,desc")).thenReturn(List.of(productEntityTest));
//        this.mockMvc.perform(get("/products/filter?category=1&min_price=0.99&max_price=120.99&is_discount=true&sort=price,desc"))
//                .andDo(print()) //печать лога вызова
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$..productId").exists())
//                .andExpect(jsonPath("$..name").exists())
//                .andExpect(jsonPath("$..productId").value(1));
//    }

    @Test
    void createProductTest() throws Exception {
        ProductResponseDto createProductResponseDtoTest = new ProductResponseDto(
                null,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                categoryResponseDtoTest);

        when(productRepositoryMock.save(any(ProductEntity.class))).thenReturn(productEntityTest);
        this.mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductResponseDtoTest))) // jackson: object -> json
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
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
}
