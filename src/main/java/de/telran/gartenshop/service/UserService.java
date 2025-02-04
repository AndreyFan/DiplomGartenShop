package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.requestDto.UserUpdateDto;
import de.telran.gartenshop.entity.CartEntity;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.entity.enums.Role;
import de.telran.gartenshop.mapper.Mappers;
import de.telran.gartenshop.repository.CartRepository;
import de.telran.gartenshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private Mappers mappers;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository, Mappers mappers) {
        this.userRepository = userRepository;
        this.mappers = mappers;
    }

    public boolean registerUser(UserRequestDto userRequestDto) {

//        UserEntity usersEntity = mappers.convertToUserEntity(userRequestDto);
//        UserEntity returnUserEntity = userRepository.save(usersEntity);
//        return returnUserEntity.getUserId() != 0;

        // checking : if user already exist ?
        UserEntity userEntityExist = userRepository.findByEmail(userRequestDto.getEmail());
        if (userEntityExist != null) {
            throw new RuntimeException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDto.getName());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setPhone(userRequestDto.getPhone());
        userEntity.setRole(Role.CLIENT);

        CartEntity cart = new CartEntity();  // create Cart for this User
        cart.setUser(userEntity);
        userEntity.setCart(cart);

        UserEntity registeredUser;
        try {
            registeredUser = userRepository.save(userEntity);
        } catch (Exception exception) {
            throw new RuntimeException("Saving user was not successful");
        }
        return registeredUser != null;

    }

    public void registerAdmin(UserRequestDto userRequestDto) {
    }

    public Boolean updateUser(UserUpdateDto userUpdateDto, Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("This User not found ");
        }

        user.setName(userUpdateDto.getName());
        user.setPhone(userUpdateDto.getPhone());

        UserEntity userUpdate;

        try {
            userUpdate = userRepository.save(user);
        } catch (Exception exception) {
            throw new RuntimeException("Error update user");
        }

        return userUpdate != null;
    }

    public void deleteUser(Long userId) {
    }

    public UserRequestDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("This User not exist");
        }

        UserRequestDto foundedUser = new UserRequestDto();
        foundedUser.setName(user.getName());
        foundedUser.setEmail(user.getEmail());
        foundedUser.setPhone(user.getPhone());
        return foundedUser;
    }

    public UserRequestDto getUserByEmail(String email) {

        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("This User not exist");
        }

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName(user.getName());
        userRequestDto.setPhone(user.getPhone());
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setPassword("******");
        return userRequestDto;
    }
}
