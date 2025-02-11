package de.telran.gartenshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.entity.CategoryEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
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

    //@MockBean
    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProductsTest() throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        when(productRepositoryMock.findAll()).thenReturn(List.of(new ProductEntity(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                new CategoryEntity(1L, "Category", null))));

        this.mockMvc.perform(get("/products"))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..productId").exists())
                .andExpect(jsonPath("$..name").exists());
    }

//    @Test
//    void getProductByIdTtst() {
//        Long testId = 1L;
//        when(productRepositoryMock.findById(testId)).thenReturn(Optional.of(productEntityTest1));
//        when(mappersMock.convertToProductDto(productEntityTest1)).thenReturn(productDtoTest1);
//    }
}
