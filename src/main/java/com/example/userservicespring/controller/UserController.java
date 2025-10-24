package com.example.userservicespring.controller;

import com.example.userservicespring.dto.CreateUserRequestDto;
import com.example.userservicespring.dto.UserDto;
import com.example.userservicespring.entity.User;
import com.example.userservicespring.exception.UserNotFoundException;
import com.example.userservicespring.mapper.UserMapper;
import com.example.userservicespring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Управление пользователями", description = "API для CRUD-операций с пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение пользователя по ID", description = "Находит и возвращает пользователя по его уникальному идентификатору")
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User userEntity = userService.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        UserDto userDto = UserMapper.toDto(userEntity);

        userDto.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete_user"));

        return userDto;
    }

    @Operation(summary = "Создание нового пользователя", description = "Регистрирует нового пользователя в системе и отправляет событие в Kafka")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserRequestDto requestDto) {
        User savedUserEntity = userService.save(requestDto);

        UserDto userDto = UserMapper.toDto(savedUserEntity);

        userDto.add(linkTo(methodOn(UserController.class).getUserById(savedUserEntity.getId())).withSelfRel());
        userDto.add(linkTo(methodOn(UserController.class).deleteUser(savedUserEntity.getId())).withRel("delete_user"));

        return userDto;
    }

    @Operation(summary = "Обновление данных пользователя", description = "Позволяет обновить существующего пользователя по его ID")
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody CreateUserRequestDto requestDto) {
        return userService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя", description = "Удаляет пользователя из системы по его ID и отправляет событие в Kafka")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}