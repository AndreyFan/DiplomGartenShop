package de.telran.gartenshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private ObjectMapper objectMapper;

    Long productIdTest;
    private ProductEntity productEntityTest;
    //private ProductResponseDto productResponseDtoTest;

    @BeforeEach
    void setUp() {
       productIdTest = 1L;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        productEntityTest = new ProductEntity(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                new CategoryEntity(1L, "CategoryName", null));

//        productResponseDtoTest = new ProductResponseDto(
//                1L,
//                "ProductName",
//                "ProductDescription",
//                new BigDecimal("10.25"),
//                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
//                new BigDecimal("8.50"),
//                timestamp,
//                timestamp,
//                new CategoryResponseDto(1L, "CategoryName"));
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

//    @Test
//    void deleteProductTest() throws Exception {
//        when(productRepositoryMock.deleteProduct().findById(inputId)).thenReturn(Optional.ofNullable(null));
//        this.mockMvc.perform(delete("/products/{productId}", productIdTest))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}
