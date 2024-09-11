package com.example.ipwa02_07.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.InputStream;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;

@Named
@ApplicationScoped
public class PageAccessBean {

    @Inject
    private LoginBean loginBean;

    private Map<String, Object> accessRights;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getResourceAsStream("/page-access.json")) {
            if (is != null) {
                accessRights = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
            } else {
                // Handle error
                System.err.println("Could not find page-access.json");
                accessRights = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            accessRights = new HashMap<>();
        }
    }

    public boolean hasAccessToPage(String pageName) {
        Map<String, List<String>> pageAccess = (Map<String, List<String>>) accessRights.get("pageAccess");
        List<String> requiredRoles = pageAccess.get(pageName);
        if (requiredRoles == null || requiredRoles.contains("ALL")) {
            return true;
        }
        for (String role : requiredRoles) {
            if (loginBean.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean canViewAllTestCases() {
        Map<String, List<String>> testCasesAccess = (Map<String, List<String>>) accessRights.get("testCases");
        List<String> rolesWithFullAccess = testCasesAccess.get("fullAccess");
        for (String role : rolesWithFullAccess) {
            if (loginBean.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    public void checkAccess(String pageName) {
        if (!hasAccessToPage(pageName)) {
            FacesContext context = FacesContext.getCurrentInstance();
            NavigationHandler handler = context.getApplication().getNavigationHandler();
            handler.handleNavigation(context, null, "/accessDenied.xhtml?faces-redirect=true");
        }
    }

    public boolean shouldRenderMenuItem(String pageName) {
        return hasAccessToPage(pageName);
    }
}