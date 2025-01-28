package de.telran.gartenshop.controller;

import de.telran.gartenshop.dto.requestDto.UserRequestDto;
import de.telran.gartenshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void registerUser(@RequestBody UserRequestDto userRequestDto) {
        userService.registerUser(userRequestDto);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateUser(@RequestBody UserRequestDto userRequestDto){
        userService.updateUser(userRequestDto);
    }

    @DeleteMapping("/{userId}")
@ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }


}