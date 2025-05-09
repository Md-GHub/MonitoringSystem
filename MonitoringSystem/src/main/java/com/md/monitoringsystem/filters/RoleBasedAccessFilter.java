package com.md.monitoringsystem.filters;

import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.exception.UnAuthorizedUser;
import com.md.monitoringsystem.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RoleBasedAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        System.out.println("Called once");

        if(session == null) {
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\"Session expired login again\"}");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole().toString();

        if(url.contains("/admin") && role.equals(Role.ADMIN.toString())){
            servletRequest.setAttribute("user", user);
            filterChain.doFilter(servletRequest, servletResponse);
        }else if(url.contains("/users/monitors") &&
                role.equals(Role.OPERATOR.toString())){
            servletRequest.setAttribute("user", user);
            filterChain.doFilter(servletRequest, servletResponse);
        }else if(url.contains("/users/monitors") && role.equals(Role.VIEWER.toString())){
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
