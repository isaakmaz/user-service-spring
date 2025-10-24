package com.example.userservicespring.dto;

import org.springframework.hateoas.RepresentationModel;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> {

    private final Long id;
    private final String name;
    private final String email;
    private final Integer age;

    public UserDto(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(name, userDto.name) && Objects.equals(email, userDto.email) && Objects.equals(age, userDto.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }
}