package com.example.ipwa02_07.beans;

import com.example.ipwa02_07.entities.User;
import com.example.ipwa02_07.services.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private UserService userService;

    @Inject
    private FacesContext facesContext;

    private String username;
    private String password;
    private User currentUser;

    public String login() {
        User user = userService.authenticate(username, password);
        if (user != null) {
            if (user.isActive()) {
                currentUser = user;
                return redirectBasedOnRole(user.getRole());
            } else {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account is inactive", null));
                return null;
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", null));
            return null;
        }
    }

    private String redirectBasedOnRole(User.UserRole role) {
        return switch (role) {
            case ADMIN -> "/user.xhtml?faces-redirect=true";
            case REQUIREMENTS_ENGINEER -> "/dashboard.xhtml?faces-redirect=true";
            case TEST_MANAGER -> "/dashboard.xhtml?faces-redirect=true";
            case TEST_CREATOR -> "/dashboard.xhtml?faces-redirect=true";
            case TESTER -> "/dashboard.xhtml?faces-redirect=true";
            default -> "/dashboard.xhtml?faces-redirect=true";
        };
    }

    public String logout() {
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();

        facesContext.getViewRoot().getViewMap().clear();

        return "/login.xhtml?faces-redirect=true";
    }

    public boolean hasRole(String roleName) {
        return currentUser != null && currentUser.getRole().name().equals(roleName);
    }

    // Getters and setters for username, password, and currentUser
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}