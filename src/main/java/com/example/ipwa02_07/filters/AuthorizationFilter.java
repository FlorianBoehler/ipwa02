package com.example.ipwa02_07.filters;

import com.example.ipwa02_07.beans.LoginBean;
import com.example.ipwa02_07.entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"*.xhtml"})
public class AuthorizationFilter implements Filter {

    @Inject
    private LoginBean loginBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            String reqURI = reqt.getRequestURI();

            if (reqURI.contains("/login.xhtml") || reqURI.contains("javax.faces.resource")) {
                chain.doFilter(request, response);
            } else if (loginBean != null && loginBean.getCurrentUser() != null) {
                if (hasAccess(reqURI, loginBean.getCurrentUser())) {
                    chain.doFilter(request, response);
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                resp.sendRedirect(reqt.getContextPath() + "/login.xhtml");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean hasAccess(String reqURI, User user) {
        User.UserRole role = user.getRole();
        if (role == User.UserRole.ADMIN) {
            return true; // Admin has access to everything
        }
        if (reqURI.contains("/admin/") && role != User.UserRole.ADMIN) {
            return false;
        }
        if (reqURI.contains("/requirements/") && role != User.UserRole.REQUIREMENTS_ENGINEER) {
            return false;
        }
        if (reqURI.contains("/testmanager/") && role != User.UserRole.TEST_MANAGER) {
            return false;
        }
        if (reqURI.contains("/testcreator/") && role != User.UserRole.TEST_CREATOR) {
            return false;
        }
        if (reqURI.contains("/tester/") && role != User.UserRole.TESTER) {
            return false;
        }
        return true; // Allow access to common pages
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}