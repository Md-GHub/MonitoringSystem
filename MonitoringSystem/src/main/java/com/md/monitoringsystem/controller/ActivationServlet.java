package com.md.monitoringsystem.controller;

import com.md.monitoringsystem.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ActivationServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        try{
            userService.activateUser(id);
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Activated Successfully\"}");
        }catch (Exception e){
            System.out.println(e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"unsuccessfully\"}");
        }
    }

}
