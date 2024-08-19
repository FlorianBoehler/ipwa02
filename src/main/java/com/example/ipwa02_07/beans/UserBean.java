package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.services.UserService;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class UserBean implements Serializable {

    @Inject
    private UserService userService;

    private User user = new User();
    private List<User> users;
    private User selectedUser;

    @PostConstruct
    public void init() {
        loadUsers();
    }

    public void loadUsers() {
        users = userService.getAllUsers();
    }

    public void saveUser() {
        try {
            if (user.getId() == null) {
                userService.createUser(user);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User created successfully"));
            } else {
                userService.updateUser(user);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated successfully"));
            }
            loadUsers(); // Reload the user list
            user = new User(); // Reset the form
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save user: " + e.getMessage()));
        }
    }

    public void deleteUser() {
        try {
            userService.deleteUser(user.getId());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User deleted successfully"));
            loadUsers();
            clearForm();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete user: " + e.getMessage()));
        }
    }

    public void onUserSelect() {
        if (selectedUser != null) {
            user = new User(selectedUser); // Create a copy to avoid modifying the original directly
        }
    }

    public void clearForm() {
        user = new User();
        selectedUser = null;
    }

    public List<User.UserRole> getAvailableRoles() {
        return Arrays.asList(User.UserRole.values());
    }

    // Getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<User> getUsers() { return users; }
    public User getSelectedUser() { return selectedUser; }
    public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }
}