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

    private Map<String, List<String>> pageAccessRights;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        pageAccessRights = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream("/page-access.json")) {
            if (is != null) {
                pageAccessRights = objectMapper.readValue(is, new TypeReference<Map<String, List<String>>>() {});
            } else {

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public boolean hasAccessToPage(String pageName) {
        List<String> requiredRoles = pageAccessRights.get(pageName);
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