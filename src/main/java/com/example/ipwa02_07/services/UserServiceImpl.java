package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User authenticate(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public User createUser(User user, String plainPassword) {
        user.setPassword(plainPassword); // This will hash the password in the User entity
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return entityManager.merge(user);
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            user.setActive(true);
            entityManager.merge(user);
        }
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            user.setActive(false);
            entityManager.merge(user);
        }
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public List<User> getActiveUsers() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.active = true", User.class).getResultList();
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = getUserById(userId);
        if (user != null) {
            user.setPassword(newPassword); // This will hash the new password in the User entity
            entityManager.merge(user);
        }
    }
}