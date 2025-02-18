package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.requestDto.UserUpdateDto;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.exception.UserAlreadyExistsException;
import de.telran.gartenshop.repository.CartRepository;
import de.telran.gartenshop.repository.UserRepository;
import de.telran.gartenshop.service.UserService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository; // Мокируем репозиторий, чтобы избежать работы с БД

    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private UserService userService;

    @MockBean
    private CartRepository cartRepository;

    @Test
    void testRegisterUser() throws Exception {
        UserRequestDto userRequest = new UserRequestDto();
        userRequest.setName("Test Name");
        userRequest.setEmail("testName@example.com");
        userRequest.setPhone("1234567890");
        userRequest.setPassword("asdfg");

        when(userRepository.findByEmail("testName@example.com")).thenReturn(null);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequest.getName());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPhone(userRequest.getPhone());
        userEntity.setRole(Role.CLIENT);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));

        verify(userRepository).findByEmail("testName@example.com");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setName("Updated Name");
        updateDto.setPhone("2222222222");

        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setName("Old Name");
        user.setPhone("1111111111");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(UserEntity.class));
    }


    @Test
    void testGetUserByEmail() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setName("maxim");
        user.setEmail("maxim@example.com");

        when(userRepository.findByEmail("maxim@example.com")).thenReturn(user);

        mockMvc.perform(get("/users/get")
                        .param("email", "maxim@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("maxim"))
                .andExpect(jsonPath("$.email").value("maxim@example.com"));

        verify(userRepository).findByEmail("maxim@example.com");
    }

    @Test
    void testGetUserById() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setName("Getbyid");
        user.setEmail("getbyid@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Getbyid"))
                .andExpect(jsonPath("$.email").value("getbyid@example.com"));

        verify(userRepository).findById(1L);
    }

    @Test
    void testDeleteUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setEmail("delete@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userRepository).deleteById(1L);
    }


}


