package com.md.monitoringsystem.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.exception.Security;
import com.md.monitoringsystem.exception.UserNotInvited;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.repo.OrgRepo;
import com.md.monitoringsystem.repo.RoleRepo;
import com.md.monitoringsystem.repo.UserRepo;
import com.md.monitoringsystem.repo.UserRoleRepo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class SecurityFilter implements Filter {
   private OrgRepo orgRepo = OrgRepo.get();
   private UserRepo userRepo = UserRepo.get();
   private RoleRepo roleRepo = RoleRepo.get();
   private UserRoleRepo userRoleRepo = UserRoleRepo.get();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        BufferedReader reader = req.getReader();
        StringBuilder inputLine = new StringBuilder();
        while(reader.ready()){
            inputLine.append(reader.readLine());
        }
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(inputLine.toString(), User.class);
        String name = user.getEmail().split("@")[1];



        if(orgRepo.isExist(name) == -1){
            req.setAttribute("user", user);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/createorg");
            requestDispatcher.forward(req, servletResponse);
        }else{
            try{
                User userFromDb = userRepo.getUserDetails(user.getEmail());

                if(userFromDb == null){
                    throw new UserNotInvited("User not invited");
                }
                if(userFromDb.isActive() == true) {
                    int roleId = userRoleRepo.getRoleIdByUserId(userFromDb.getUserId());
                    Role role = roleRepo.getRoleById(roleId);
                    userFromDb.setUserName(user.getUserName());
                    userFromDb.setPassword(user.getPassword());
                    userFromDb.setRole(role);
                    servletRequest.setAttribute("user", userFromDb);
                    filterChain.doFilter(servletRequest, servletResponse);
                }else{
                    throw new Security("Change the password first before login");
                }
            }catch(UserNotInvited e){
                resp.setContentType("application/json");
                resp.setStatus(404);
                resp.getWriter().print("{\"status\":\"failure\",\"message\":\"" + e.getMessage() + "\"}");
            }catch (Security e){
                resp.setContentType("application/json");
                resp.setStatus(403);
                resp.getWriter().print("{\"status\":\"failure\",\"message\":\"" + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
