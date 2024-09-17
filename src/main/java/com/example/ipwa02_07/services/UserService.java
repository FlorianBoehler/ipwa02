package com.example.ipwa02_07.services;

import com.example.ipwa02_07.entities.User;
import java.util.List;

public interface UserService {
    User authenticate(String username, String password);
    User createUser(User user, String plainPassword);
    User updateUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User getUserByUsername(String username);

}