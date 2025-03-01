package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestdto.UserRequestDto;
import de.telran.gartenshop.dto.requestdto.UserUpdateDto;
import de.telran.gartenshop.dto.responsedto.UserResponseDto;
import de.telran.gartenshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController implements UserControllerInterface {
private static final String STARS = "******";
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // The method is used to register a new Client
    // http://localhost:8088/users/register
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Boolean registerUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.registerUser(userRequestDto);
    }

    // The method is used to register a new Admin
    // http://localhost:8088/users/registerAdmin
    @PostMapping("/registerAdmin")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Boolean registerAdmin(@RequestBody UserRequestDto userRequestDto) {
        return userService.registerAdmin(userRequestDto);
    }

    @GetMapping(value = "/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserResponseDto getUserById(@PathVariable Long userId) {
        UserResponseDto userResponseDto =userService.getUserById(userId);
        userResponseDto.setPasswordHash(STARS);
        userResponseDto.setRefreshToken(STARS);
        return userResponseDto;
    }

    // search for user by his email
    // http://localhost:8088/users/get?email=alice.smith@example.com
    @GetMapping(value = "/get")
    public UserResponseDto getUserByEmail(@RequestParam String email) {
        UserResponseDto userResponseDto =userService.getUserByEmail(email);
        userResponseDto.setPasswordHash(STARS);
        userResponseDto.setRefreshToken(STARS);
        return userResponseDto;
    }

    @PutMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Boolean updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long userId) {

        return userService.updateUser(userUpdateDto, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}