package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.entity.CartEntity;
import de.telran.gartenshop.entity.UserEntity;
import de.telran.gartenshop.entity.enums.Role;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserRequestDto userRequestDto) {
        // checking : if user already exist ?
//        UserEntity userEntityExist = userRepository.findByEmail(userRequestDto.getEmail()).orElse(null);
//        if (userEntityExist != null){
//            throw new RuntimeException("User already exist");
//        }

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
        } catch (Exception exception) {
            throw new RuntimeException("Saving user was not successful");
        }

    }

    public void registerAdmin(UserRequestDto userRequestDto) {
    }

    public void updateUser(UserRequestDto userRequestDto) {
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
}
