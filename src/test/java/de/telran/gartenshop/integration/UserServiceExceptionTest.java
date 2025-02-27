package de.telran.gartenshop.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.telran.gartenshop.dto.requestdto.UserRequestDto;
import de.telran.gartenshop.dto.requestdto.UserUpdateDto;
import de.telran.gartenshop.entity.CartEntity;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.exception.UserAlreadyExistsException;
import de.telran.gartenshop.exception.UserDeleteException;
import de.telran.gartenshop.exception.UserNotFoundException;
import de.telran.gartenshop.exception.UserSaveException;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CartRepository;
import de.telran.gartenshop.repository.UserRepository;
import de.telran.gartenshop.security.configure.SecurityConfig;
import de.telran.gartenshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = {"CLIENT", "ADMINISTRATOR"})
class UserServiceExceptionTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private Mappers mappers;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDto userRequestDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("TestName", "testname@example.com", "1234567890", "12345");

        userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setName("TestName");
        userEntity.setEmail("testname@example.com");
        userEntity.setPhone("1234567890");
        userEntity.setRole(Role.CLIENT);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setUser(userEntity);
        userEntity.setCart(cartEntity);
    }

    @Test
    void registerUser_ShouldThrowUserAlreadyExistsException_WhenUserAlreadyExists() {
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(userEntity);
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userRequestDto));
    }

    @Test
    void testRegisterAdmin_negative_UserAlreadyExists() {
        UserRequestDto userRequest = new UserRequestDto();
        userRequest.setName("TestName");
        userRequest.setEmail("testname@example.com");
        userRequest.setPhone("1234567890");
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(userEntity);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerAdmin(userRequest));
        assertEquals("User with email testname@example.com already exists", exception.getMessage());

    }

    @Test
    void registerUser_ShouldThrowUserSaveException_WhenDatabaseFails() {
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedPassword"); // ✅ Добавили заглушку
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException());

        assertThrows(UserSaveException.class, () -> userService.registerUser(userRequestDto));
    }

    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(new UserUpdateDto("New Name", "987654321"), 1L));
    }

    @Test
    void updateUser_ShouldThrowUserSaveException_WhenDatabaseFails() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException());
        assertThrows(UserSaveException.class, () -> userService.updateUser(new UserUpdateDto("New Name", "987654321"), 1L));
    }

    @Test
    void getUserByEmail_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("unknown@example.com"));
    }

    @Test
    void getUserById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void deleteUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_ShouldThrowUserDeleteException_WhenDataIntegrityViolationOccurs() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        doThrow(new DataIntegrityViolationException("Foreign key constraint"))
                .when(cartRepository).delete(any(CartEntity.class));
        assertThrows(UserDeleteException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_ShouldThrowUserDeleteException_WhenUnexpectedErrorOccurs() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        doThrow(new RuntimeException("Unexpected error"))
                .when(userRepository).deleteById(anyLong());
        assertThrows(UserDeleteException.class, () -> userService.deleteUser(1L));
    }
}
