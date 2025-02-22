package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.CategoryRequestDto;
import de.telran.gartenshop.dto.responseDto.CategoryResponseDto;
import de.telran.gartenshop.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequestDto categoryRequestDto;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("TestCategory");

        categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setCategoryId(1L);
        categoryResponseDto.setName("TestCategory");
    }

    @Test
    void getAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(categoryResponseDto));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("TestCategory"));
    }

    @Test
    void createCategory() throws Exception {
        when(categoryService.createCategory(any())).thenReturn(true);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCategory() throws Exception {
        when(categoryService.updateCategory(any(), any())).thenReturn(categoryResponseDto);

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCategory"));
    }

    @Test
    void deleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(any());

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());
    }
}