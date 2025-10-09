package com.example.userservicespring.service;

import com.example.userservicespring.dto.CreateUserRequestDto;
import com.example.userservicespring.dto.UserDto;
import com.example.userservicespring.entity.User;
import com.example.userservicespring.exception.UserNotFoundException;
import com.example.userservicespring.mapper.UserMapper;
import com.example.userservicespring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Важный импорт

import java.util.Optional;

@Service // Говорим, что это сервис
public class UserServiceImpl implements UserService {

    // Наша зависимость - репозиторий
    private final UserRepository userRepository;

    // Внедрим зависимость через конструктор
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Оборачиваем метод в транзакцию
    public UserDto save(CreateUserRequestDto requestDto) {
        // Конвертируем DTO в сущность
        User user = UserMapper.toEntity(requestDto);
        // Сохраняем сущность и получаем обновленную (с ID)
        User savedUser = userRepository.save(user);
        // Конвертируем сохраненную сущность обратно в DTO и возвращаем
        return UserMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true) // "только для чтения" - оптимизация
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto);
    }

    @Override
    @Transactional
    public UserDto update(Long id, CreateUserRequestDto requestDto) {
        // Находим пользователя в базе. Если его нет, выбросим исключение
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Обновляем поля существующего пользователя
        existingUser.setName(requestDto.name());
        existingUser.setEmail(requestDto.email());
        existingUser.setAge(requestDto.age());

        // Сохраняем
        User updatedUser = userRepository.save(existingUser);

        // Возвращаем
        return UserMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Проверяем, существует ли пользователь
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Cannot delete. User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}