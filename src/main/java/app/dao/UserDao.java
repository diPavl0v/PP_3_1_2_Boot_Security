package app.dao;

import app.model.User;
import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(Long id);
    void save(User user);
    void deleteById(Long id);
}
