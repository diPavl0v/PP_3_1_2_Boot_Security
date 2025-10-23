package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Set;

@Configuration
public class DataInit {

    @Bean
    CommandLineRunner init(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder enc) {
        return args -> {
            Role rAdmin = roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));
            Role rUser = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));

            if (userRepo.findByUsername("admin").isEmpty()) {
                try {
                    User a = new User();
                    a.setUsername("admin");
                    a.setPassword(enc.encode("admin"));
                    a.setRoles(Set.of(rAdmin, rUser));
                    userRepo.save(a);
                } catch (DataIntegrityViolationException ex) {
                }
            }

            if (userRepo.findByUsername("user").isEmpty()) {
                try {
                    User u = new User();
                    u.setUsername("user");
                    u.setPassword(enc.encode("user"));
                    u.setRoles(Set.of(rUser));
                    userRepo.save(u);
                } catch (DataIntegrityViolationException ex) {
                }
            }
        };
    }
}
