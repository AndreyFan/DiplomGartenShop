package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.requestDto.UserUpdateDto;
import de.telran.gartenshop.dto.responseDto.UserResponseDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mappers mappers;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public Boolean registerUser(UserRequestDto userRequestDto) {
        // checking : if user already exist ?
        UserEntity userEntityExist = userRepository.findByEmail(userRequestDto.getEmail());
        if (userEntityExist != null) {
            throw new UserAlreadyExistsException("User with email " + userRequestDto.getEmail() + " already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDto.getName());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setPhone(userRequestDto.getPhone());
        userEntity.setRole(Role.CLIENT);
        userEntity.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        CartEntity cart = new CartEntity();  // create Cart for this User
        cart.setUser(userEntity);
        userEntity.setCart(cart);
        try {
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            throw new UserSaveException("Failed to save user in database", e);
        }

    }

    // метод служит для регистрации нового Администратора
    public Boolean registerAdmin(UserRequestDto userRequestDto) {
        // checking : if user already exist ?
        UserEntity userEntityExist = userRepository.findByEmail(userRequestDto.getEmail());
        if (userEntityExist != null) {
            throw new UserAlreadyExistsException("User with email " + userRequestDto.getEmail() + " already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDto.getName());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setPhone(userRequestDto.getPhone());
        userEntity.setRole(Role.ADMINISTRATOR);
        userEntity.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        CartEntity cart = new CartEntity();  // create Cart for this User
        cart.setUser(userEntity);
        userEntity.setCart(cart);
        try {
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            throw new UserSaveException("Failed to save user in database", e);
        }
    }

    public Boolean updateUser(UserUpdateDto userUpdateDto, Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        user.setName(userUpdateDto.getName());
        user.setPhone(userUpdateDto.getPhone());
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new UserSaveException("Failed to save userUpdate in database", e);
        }
    }

    public UserResponseDto getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        UserResponseDto foundedUser = mappers.convertToUserResponseDto(user);
        foundedUser.setPasswordHash("******");
        return foundedUser;
    }

    public UserResponseDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        UserResponseDto foundedUser = mappers.convertToUserResponseDto(user);
        foundedUser.setPasswordHash("******");
        return foundedUser;
    }


    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        try {
            // Удаляем корзину пользователя, если она есть
            if (user.getCart() != null) {
                cartRepository.delete(user.getCart());
            }
            // Удаляем пользователя
            userRepository.deleteById(userId);
        } catch (DataIntegrityViolationException e) {
            throw new UserDeleteException("Cannot delete user with ID " + userId + " due to existing related records", e);
        } catch (Exception e) {
            throw new UserDeleteException("Unexpected error occurred while deleting user with ID " + userId, e);
        }
    }

    public UserResponseDto getByRefreshToken(String token) {
        UserEntity usersEntity = userRepository.getByRefreshToken(token).stream().findFirst().orElse(null);
        return mappers.convertToUserResponseDto(usersEntity);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserRefreshToken(UserResponseDto user, String refreshToken) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        userEntity.setRefreshToken(refreshToken);
        UserEntity refreshUsersEntity =  userRepository.save(userEntity);
    }
}
