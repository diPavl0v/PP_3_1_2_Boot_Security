package ru.kata.spring.boot_security.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInit {
    @Bean
    CommandLineRunner init(RoleRepository roles, UserRepository users, PasswordEncoder enc){
        return args -> {
            Role rAdmin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            Role rUser  = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("ROLE_USER")));

            users.findByUsername("admin").orElseGet(() -> {
                User a = new User();
                a.setUsername("admin"); a.setPassword(enc.encode("admin"));
                a.setRoles(Set.of(rAdmin, rUser));
                return users.save(a);
            });

            users.findByUsername("user").orElseGet(() -> {
                User u = new User();
                u.setUsername("user"); u.setPassword(enc.encode("user"));
                u.setRoles(Set.of(rUser));
                return users.save(u);
            });
        };
    }
}
