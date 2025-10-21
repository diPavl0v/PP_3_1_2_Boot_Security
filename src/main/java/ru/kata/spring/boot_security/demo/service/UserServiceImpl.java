package ru.kata.spring.boot_security.demo.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + id));
    }

    @Override
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
    }

    @Override
    public User update(Long id, User patch) {
        User db = findById(id);

        if (patch.getUsername() != null && !patch.getUsername().equals(db.getUsername())) {
            if (userRepository.existsByUsernameAndIdNot(patch.getUsername(), id)) {
                throw new IllegalArgumentException("Username already exists: " + patch.getUsername());
            }
            db.setUsername(patch.getUsername());
        }

        if (patch.getFirstName() != null) db.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null) db.setLastName(patch.getLastName());
        if (patch.getAge() != null) db.setAge(patch.getAge());

        if (patch.getPassword() != null && !patch.getPassword().isBlank()) {
            db.setPassword(encoder.encode(patch.getPassword()));
        }

        if (patch.getRoles() != null && !patch.getRoles().isEmpty()) {
            db.setRoles(patch.getRoles());
        }

        try {
            return userRepository.save(db);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username already exists: " + db.getUsername());
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
