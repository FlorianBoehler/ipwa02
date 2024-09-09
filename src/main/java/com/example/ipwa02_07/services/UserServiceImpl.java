package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User authenticate(String username, String password) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.active = true", User.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (user != null && user.getPassword().equals(password)) { // In production, use proper password hashing
                return user;
            }
        } catch (NoResultException e) {
            // User not found or inactive
        }
        return null;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        user.setActive(true); // Set new users as active by default
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
        User user = entityManager.find(User.class, id);
        if (user != null) {
            user.setActive(true);
            entityManager.merge(user);
        }
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = entityManager.find(User.class, id);
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
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setPassword(newPassword); // In production, hash the password
            entityManager.merge(user);
        }
    }
}