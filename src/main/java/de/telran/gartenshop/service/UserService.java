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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mappers mappers;
    private final CartRepository cartRepository;

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
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setName(user.getName());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPasswordHash("******");
        return userResponseDto;
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

}
