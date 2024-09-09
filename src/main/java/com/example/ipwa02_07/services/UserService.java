package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.User;
import java.util.List;

public interface UserService {
    User authenticate(String username, String password);
    User createUser(User user);
    User updateUser(User user);
    void activateUser(Long id);
    void deactivateUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
    List<User> getActiveUsers();
    User getUserByUsername(String username);
    void changePassword(Long userId, String newPassword);
}