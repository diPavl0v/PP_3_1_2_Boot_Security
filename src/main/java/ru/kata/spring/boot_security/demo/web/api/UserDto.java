package ru.kata.spring.boot_security.demo.web.api;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;
import java.util.stream.Collectors;

public record UserDto(Long id, String username, String firstName, String lastName, Integer age, Set<String> roles) {
    public static UserDto from(User u) {
        return new UserDto(
                u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getAge(),
                u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet())
        );
    }
}
