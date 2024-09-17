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
    private EntityManager em;

    @Override
    public User authenticate(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.checkPassword(password) && user.isActive()) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public User createUser(User user, String plainPassword) {
        user.setPassword(plainPassword);
        em.persist(user);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return em.merge(user);
    }

    @Override
    public User getUserById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}