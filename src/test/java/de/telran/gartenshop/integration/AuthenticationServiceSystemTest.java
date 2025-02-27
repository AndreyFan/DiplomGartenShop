package de.telran.gartenshop.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.telran.gartenshop.dto.requestdto.UserRequestDto;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.UserRepository;
import de.telran.gartenshop.security.dto.JwtRequestDto;
import de.telran.gartenshop.security.dto.JwtRequestRefreshDto;
import de.telran.gartenshop.security.jwt.JwtProvider;
import de.telran.gartenshop.security.service.AuthService;
import de.telran.gartenshop.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest()
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationServiceSystemTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Mappers mappersTest;

    @Autowired
    private ObjectMapper objectMapper;

    @Order(value = 1)
    @Test
    void registerTest() throws Exception {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("testName")
                .email("testName@example.com")
                .phone("1512484546")
                .password("testPass")
                .build();

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated());
    }

    @Order(value = 2)
    @Test
    void loginTest() throws Exception {
        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("testName@example.com");
        jwtRequestDto.setPassword("testPass");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Order(value = 3)
    @Test
    void getAccessTokenTest() throws Exception {
        UserEntity user = userRepository.findByEmail("testName@example.com");
        String refreshToken = user.getRefreshToken();
        JwtRequestRefreshDto requestRefresh = new JwtRequestRefreshDto();
        requestRefresh.setRefreshToken(refreshToken);

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestRefresh)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isEmpty());
    }

    @Order(value = 4)
    @Test
    void refreshTest() throws Exception {
        // Логинимся, чтобы получить accessToken и refreshToken
        JwtRequestDto jwtRequestDto = new JwtRequestDto();
        jwtRequestDto.setEmail("testName@example.com");
        jwtRequestDto.setPassword("testPass");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();
        // Достаем токены из ответа
        String responseContent = loginResult.getResponse().getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        String accessToken = jsonResponse.get("accessToken").asText();
        String refreshToken = jsonResponse.get("refreshToken").asText();

        JwtRequestRefreshDto requestRefresh = new JwtRequestRefreshDto();
        requestRefresh.setRefreshToken(refreshToken);
        // Тестируем /auth/refresh с заголовком Authorization
        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken) // Добавили токен
                        .content(objectMapper.writeValueAsString(requestRefresh)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }
}
