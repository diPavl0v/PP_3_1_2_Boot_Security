package ru.kata.spring.boot_security.demo.web.api;

import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleRepository roles;

    public RoleRestController(RoleRepository roles) {
        this.roles = roles;
    }

    @GetMapping
    public Iterable<Role> all() {
        return roles.findAll();
    }
}
