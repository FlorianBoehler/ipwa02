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
    private String newPassword;
    private String confirmPassword;
    private boolean isFirstUser;

    @PostConstruct
    public void init() {
        loadUsers();
        checkIfFirstUser();
    }

    public void loadUsers() {
        users = userService.getAllUsers();
    }

    private void checkIfFirstUser() {
        this.isFirstUser = users.isEmpty();
    }

    public void saveUser() {
        try {
            if (user.getId() == null) {
                if (newPassword == null || newPassword.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password is required for new users"));
                    return;
                }
                user = userService.createUser(user, newPassword);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User created successfully"));
            } else {
                user = userService.updateUser(user);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User updated successfully"));
            }
            loadUsers();
            clearForm();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save user: " + e.getMessage()));
        }


    }

    public void createFirstAdminUser() {
        if (!newPassword.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Passwords do not match"));
            return;
        }

        user.setRole(User.UserRole.ADMIN);
        user.setActive(true);

        try {
            user = userService.createUser(user, newPassword);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Admin user created successfully"));
            loadUsers();
            clearForm();
            isFirstUser = false;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create admin user: " + e.getMessage()));
        }
    }

    public void onUserSelect() {
        if (selectedUser != null) {
            user = new User(selectedUser);
            newPassword = null;
            confirmPassword = null;
        }
    }

    public void clearForm() {
        user = new User();
        selectedUser = null;
        newPassword = null;
        confirmPassword = null;
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

    public String getNewPassword() { return newPassword; }

    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }

    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public boolean getIsFirstUser() { return isFirstUser; }

    public void setIsFirstUser(boolean isFirstUser) { this.isFirstUser = isFirstUser; }
}