package app.service;

import app.model.User;
import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(Long id);
    User save(User u);
    void delete(Long id);
}
