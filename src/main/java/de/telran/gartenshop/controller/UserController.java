package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.dto.requestDto.UserUpdateDto;
import de.telran.gartenshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getTest() {
        return "Test Task_2";
    }


    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public boolean registerUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.registerUser(userRequestDto);
    }

    @GetMapping(value = "/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserRequestDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/get")
    public UserRequestDto getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }


    // метод служит для изменения роли, так как при регистрации нового человека
    // ему присваивается роль "Клиент"
    // http://localhost:8088/users/registerAdmin?email=nina.green@example.com
    @PutMapping("/registerAdmin")
    public Boolean registerAdmin(@RequestParam String email) {
        return userService.registerAdmin(email);
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