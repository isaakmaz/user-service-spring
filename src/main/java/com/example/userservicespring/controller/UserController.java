package com.example.userservicespring.controller;

import com.example.userservicespring.dto.CreateUserRequestDto;
import com.example.userservicespring.dto.UserDto;
import com.example.userservicespring.exception.UserNotFoundException;
import com.example.userservicespring.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; // Важный импорт

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @PostMapping
    public UserDto createUser(@RequestBody CreateUserRequestDto requestDto) {
        return userService.save(requestDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody CreateUserRequestDto requestDto) {
        return userService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Указываем Spring всегда возвращать статус 204
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}
