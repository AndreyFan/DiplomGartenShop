package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestdto.UserRequestDto;
import de.telran.gartenshop.dto.requestdto.UserUpdateDto;
import de.telran.gartenshop.dto.responsedto.UserResponseDto;
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

@Service
@RequiredArgsConstructor
public class UserService {
    private static final  String USER_WITH_EMAIL ="User with email ";
    private static final  String USER_WITH_ID ="User with ID ";
    private static final  String NOT_FOUND=" not found";

    private final UserRepository userRepository;
    private final Mappers mappers;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Boolean registerUser(UserRequestDto userRequestDto) {
        // checking : if user already exist ?
        UserEntity userEntityExist = userRepository.findByEmail(userRequestDto.getEmail());
        if (userEntityExist != null) {
            throw new UserAlreadyExistsException(USER_WITH_EMAIL + userRequestDto.getEmail() + " already exists");
        }
        UserEntity userEntity = mappers.convertToUserEntity(userRequestDto);
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

    // The method is used to register a new Admin
    @Transactional
    public Boolean registerAdmin(UserRequestDto userRequestDto) {
        // checking : if user already exist ?
        UserEntity userEntityExist = userRepository.findByEmail(userRequestDto.getEmail());
        if (userEntityExist != null) {
            throw new UserAlreadyExistsException(USER_WITH_EMAIL + userRequestDto.getEmail() + " already exists");
        }
        UserEntity userEntity = mappers.convertToUserEntity(userRequestDto);
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
            throw new UserNotFoundException(USER_WITH_ID + userId + NOT_FOUND);
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
            throw new UserNotFoundException(USER_WITH_EMAIL + email + NOT_FOUND);
        }
        return mappers.convertToUserResponseDto(user);
    }

    public UserResponseDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(USER_WITH_ID + userId + NOT_FOUND);
        }
        return  mappers.convertToUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(USER_WITH_ID + userId + NOT_FOUND);
        }
        try {
            // First, delete the user's recycle bin if it exists.
            if (user.getCart() != null) {
                cartRepository.delete(user.getCart());
            }
            // Then we delete the user
            userRepository.deleteById(userId);
        } catch (DataIntegrityViolationException e) {
            throw new UserDeleteException("Cannot delete user with ID " + userId + " due to existing related records", e);
        } catch (Exception e) {
            throw new UserDeleteException("Unexpected error occurred while deleting user with ID " + userId, e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserRefreshToken(UserResponseDto user, String refreshToken) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());
        userEntity.setRefreshToken(refreshToken);
        try {
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new UserSaveException("Failed to save userUpdate in database", e);
        }
    }
}
