package com.example.userservicespring.service;

import com.example.userservicespring.dto.CreateUserRequestDto;
import com.example.userservicespring.dto.EmailNotificationDto;
import com.example.userservicespring.dto.UserDto;
import com.example.userservicespring.entity.User;
import com.example.userservicespring.exception.UserNotFoundException;
import com.example.userservicespring.kafka.KafkaProducerService;
import com.example.userservicespring.mapper.UserMapper;
import com.example.userservicespring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    public UserServiceImpl(UserRepository userRepository, KafkaProducerService kafkaProducerService) {
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Transactional
    public User save(CreateUserRequestDto requestDto) {
        User user = UserMapper.toEntity(requestDto);
        User savedUser = userRepository.save(user);

        String subject = "Добро пожаловать!";
        String body = String.format("Здравствуйте, %s! Ваш аккаунт на сайте 'Мой Супер Сайт' был успешно создан.", savedUser.getName());
        EmailNotificationDto notification = new EmailNotificationDto(savedUser.getEmail(), subject, body);

        kafkaProducerService.sendEmailNotification(notification);

        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    @Transactional
    public UserDto update(Long id, CreateUserRequestDto requestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setName(requestDto.name());
        existingUser.setEmail(requestDto.email());
        existingUser.setAge(requestDto.age());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Cannot delete. User not found with id: " + id));

        userRepository.deleteById(id);

        String subject = "Ваш аккаунт был удален";
        String body = String.format("Здравствуйте, %s! Ваш аккаунт был удалён с сайта 'Мой Супер Сайт'.", userToDelete.getName());
        EmailNotificationDto notification = new EmailNotificationDto(userToDelete.getEmail(), subject, body);

        kafkaProducerService.sendEmailNotification(notification);
    }

}