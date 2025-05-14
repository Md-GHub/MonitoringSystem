package com.md.monitoringsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.exception.NoUserFounded;
import com.md.monitoringsystem.model.LoginCred;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ActivationServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int id = Integer.parseInt(req.getParameter("id"));
        BufferedReader reader = req.getReader();
        StringBuilder inputLine = new StringBuilder();
        while(reader.ready()){
            inputLine.append(reader.readLine());
        }
        ObjectMapper mapper = new ObjectMapper();
        LoginCred emailPassword = mapper.readValue(inputLine.toString(), LoginCred.class);

        if(!isValidPassword(emailPassword.getPassword())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\"Not an valid password\"}");
            return;
        }

        try{
//            userService.activateUser(id);
            userService.activateUser(emailPassword);
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Activated Successfully\"}");
        }catch (NoUserFounded e){
            System.out.println(e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"unsuccess\",\"message\":\""+e.getMessage()+"\"}");
        }catch (Exception e){
            System.out.println(e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(SC_BAD_REQUEST);
            resp.getWriter().print("{\"status\":\"unsuccess\",\"message\":\""+e.getMessage()+"\"}");
        }
    }
    private static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }

        return hasLetter && hasDigit && hasSpecialChar;
    }


}
