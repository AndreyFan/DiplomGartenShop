package de.telran.gartenshop.service;

import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.mapper.Mappers;
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
    public UserService(UserRepository userRepository, Mappers mappers) {
        this.userRepository = userRepository;
        this.mappers = mappers;
    }

    public void registerUser(UserRequestDto userRequestDto) {
    }

    public void registerAdmin(UserRequestDto userRequestDto) {
    }

    public void updateUser(UserRequestDto userRequestDto) {
    }

    public void deleteUser(Long userId) {
    }

}
