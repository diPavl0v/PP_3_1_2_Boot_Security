package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findById(Long id);
    User create(User u);
    User update(Long id, User u);
    void delete(Long id);
}
