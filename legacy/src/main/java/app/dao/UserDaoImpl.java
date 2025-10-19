package app.dao;

import app.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) em.persist(user);
        else em.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        User u = em.find(User.class, id);
        if (u != null) em.remove(u);
    }
}
