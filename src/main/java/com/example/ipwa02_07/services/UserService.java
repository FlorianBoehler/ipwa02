package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRED)
    public User createUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User updateUser(User user) {
        return entityManager.merge(user);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}