package com.md.monitoringsystem.filters;

import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.utils.JwtManager;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RoleBasedAccessFilter implements Filter {
    private JwtManager jwtManager = new JwtManager();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        System.out.println("Called once");
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                break;
            }
        }
        System.out.println(token);
        if(token == null || !jwtManager.validateToken(token)) {
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\"Session expired login again\"}");
            return;
        }
        int userId = jwtManager.getUserId(token);
        String userName = jwtManager.getUsername(token);
        String userEmail = jwtManager.getUsername(token);
        Role role = jwtManager.getUserRole(token);
        String method = request.getMethod();
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setEmail(userEmail);
        user.setRole(role);
        user.setEmail(userEmail);

        if((url.contains("/admin") || url.contains("/users/monitors")) && role.equals(Role.ADMIN)){
            servletRequest.setAttribute("user", user);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }else if(url.contains("/users/monitors") &&
                role.equals(Role.OPERATOR) &&
                ("GET".equals(method) || "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) {
            servletRequest.setAttribute("user", user);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }else if(url.contains("/users/monitors") && role.equals(Role.VIEWER) && "GET".equals(method)){
            servletRequest.setAttribute("user", user);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        resp.setContentType("application/json");
        resp.setStatus(401);
        resp.getWriter().print("{\"status\":\"failure\",\"message\":\"unauthorized access\"}");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
