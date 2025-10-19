package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service @Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, PasswordEncoder encoder) {
        this.users = users; this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override public List<User> findAll(){ return users.findAll(); }
    @Override public User findById(Long id){ return users.findById(id).orElseThrow(); }

    @Transactional @Override
    public User create(User u){
        u.setPassword(encoder.encode(u.getPassword()));
        return users.save(u);
    }

    @Transactional @Override
    public User update(Long id, User data){
        User u = findById(id);
        u.setUsername(data.getUsername());
        if (data.getPassword()!=null && !data.getPassword().isBlank())
            u.setPassword(encoder.encode(data.getPassword()));
        u.setFirstName(data.getFirstName());
        u.setLastName(data.getLastName());
        u.setAge(data.getAge());
        u.setRoles(data.getRoles());
        return u;
    }

    @Transactional @Override
    public void delete(Long id){ users.deleteById(id); }
}
