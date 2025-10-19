package app.service;

import app.model.User;
import app.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    public UserServiceImpl(UserRepository repo){ this.repo = repo; }

    @Override public List<User> getAll(){ return repo.findAll(); }
    @Override public User getById(Long id){ return repo.findById(id).orElse(null); }
    @Override public User save(User u){ return repo.save(u); }
    @Override public void delete(Long id){ repo.deleteById(id); }
}
