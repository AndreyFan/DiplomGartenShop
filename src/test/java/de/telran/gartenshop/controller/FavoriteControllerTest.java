package de.telran.gartenshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestDto.FavoriteRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.dto.responseDto.FavoriteResponseDto;
import de.telran.gartenshop.dto.responseDto.ProductResponseDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc; // для имитации запросов пользователей

    @MockBean
    private FavoriteService favoriteServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    private FavoriteResponseDto favoriteResponseDtoTest;

    private FavoriteRequestDto favoriteRequestDtoTest;

    Timestamp timestamp;
    Long userIdTest = 1L;

    @BeforeEach
    void setUp() {
        timestamp = new Timestamp(new Date().getTime());

        ProductResponseDto productResponseDtoTest = new ProductResponseDto(
                1L,
                "ProductName",
                "ProductDescription",
                new BigDecimal("10.25"),
                "https://spec.tass.ru/geroi-multfilmov/images/header/kitten-woof.png",
                new BigDecimal("8.50"),
                timestamp,
                timestamp,
                new CategoryResponseDto(1L, "CategoryName"));

        UserResponseDto userResponseDtoTest = new UserResponseDto(
                1L,
                "Tom Smith",
                "ts@gmail.com",
                "+4975644333",
                "hgfgjfdlgjflg",
                Role.CLIENT);

        favoriteResponseDtoTest = new FavoriteResponseDto(
                1L,
                userResponseDtoTest,
                productResponseDtoTest);

        favoriteRequestDtoTest = new FavoriteRequestDto(
                1L,
                1L);
    }

    @Test
    void getFavoritesByUserIdTest() throws Exception {
        when(favoriteServiceMock.getFavoritesByUserId(userIdTest)).thenReturn(Set.of(favoriteResponseDtoTest));
        this.mockMvc.perform(get("/favorites/{userId}", userIdTest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..favoriteId").exists())
                .andExpect(jsonPath("$..favoriteId").value(1))
                .andExpect(jsonPath("$..productId").value(1))
                .andExpect(jsonPath("$..userId").value(1));
    }

    @Test
    void getFavoritesTest() throws Exception {
        when(favoriteServiceMock.getFavorites(anyString())).thenReturn(Set.of(favoriteResponseDtoTest));
        this.mockMvc.perform(get("/favorites/get?email=ts@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..favoriteId").exists())
                .andExpect(jsonPath("$..favoriteId").value(1))
                .andExpect(jsonPath("$..productId").value(1))
                .andExpect(jsonPath("$..userId").value(1));
    }

    @Test
    void createFavoriteTest() throws Exception {
        when(favoriteServiceMock.createFavorite(any(FavoriteRequestDto.class))).thenReturn(true);
        this.mockMvc.perform(post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteRequestDtoTest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteFavoriteTest() throws Exception {

        this.mockMvc.perform(delete("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteRequestDtoTest)))
                .andDo(print()) //печать лога вызова
                .andExpect(status().isOk());
        verify(favoriteServiceMock).deleteFavorite(favoriteRequestDtoTest);
    }
}