package ru.kata.spring.boot_security.demo.web.api;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Set;

public record UserUpsertDto(String username, String password, String firstName, String lastName, Integer age, Set<String> roles) {
    public User toEntity(Set<Role> resolvedRoles){
        User u = new User();
        u.setUsername(username);
        u.setPassword(password); // сервис сам заэнкодит и пропустит пустое при update
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setAge(age);
        u.setRoles(resolvedRoles);
        return u;
    }
}
