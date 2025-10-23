package ru.kata.spring.boot_security.demo.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;

    public UserRestController(UserRepository users, RoleRepository roles, PasswordEncoder encoder) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
    }

    @GetMapping
    public Iterable<User> all() {
        return users.findAll();
    }

    @GetMapping("/{id}")
    public User one(@PathVariable Long id) {
        return users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<User> create(@RequestBody User dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (users.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }

        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setAge(dto.getAge());
        u.setRoles(resolveRoles(dto.getRoles())); // принимает id или name

        User saved = users.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Transactional
    public User update(@PathVariable Long id, @RequestBody User dto) {
        User u = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (dto.getUsername() != null && !dto.getUsername().equals(u.getUsername())) {
            if (users.existsByUsernameAndIdNot(dto.getUsername(), id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
            }
            u.setUsername(dto.getUsername());
        }
        if (dto.getFirstName() != null) u.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)  u.setLastName(dto.getLastName());
        if (dto.getAge() != null)       u.setAge(dto.getAge());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(encoder.encode(dto.getPassword()));
        }
        if (dto.getRoles() != null) {
            u.setRoles(resolveRoles(dto.getRoles()));
        }
        return users.save(u);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!users.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        users.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Set<Role> resolveRoles(Set<Role> input) {
        Set<Role> out = new HashSet<>();
        if (input == null) return out;
        for (Role r : input) {
            if (r == null) continue;
            Optional<Role> found = Optional.empty();
            if (r.getId() != null) {
                found = roles.findById(r.getId());
            } else if (r.getName() != null) {
                found = roles.findByName(r.getName());
            }
            found.ifPresent(out::add);
        }
        return out;
    }
}
