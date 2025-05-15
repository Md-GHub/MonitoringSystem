package com.md.monitoringsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.service.UserService;
import com.md.monitoringsystem.utils.LoggerUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class CreateServlet extends HttpServlet {
    private UserService userService = new UserService();
    private Logger logger = LoggerUtils.getLogger();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /// read the json (username password)
        User user = (User) ((ServletRequest) req).getAttribute("user");
        if(!isValidPassword(user.getPassword())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\"Not an valid password\"}");
            return;
        }
        try{
            userService.createAdmin(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Account created successfully\"}");
        }catch(Exception e){
            System.out.println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"status\":\"error\",\"message\":\""+e.getMessage()+"\"}");
            logger.info(new Timestamp(System.currentTimeMillis())+" "+e.getMessage());
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
