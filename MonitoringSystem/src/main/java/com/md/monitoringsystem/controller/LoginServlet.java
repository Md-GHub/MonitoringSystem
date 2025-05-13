package com.md.monitoringsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.md.monitoringsystem.exception.UsernamePasswordError;
import com.md.monitoringsystem.model.User;
import com.md.monitoringsystem.service.LoginService;
import com.md.monitoringsystem.utils.JwtManager;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private LoginService loginService = new LoginService();
    private JwtManager jwtManager = new JwtManager();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object obj = req.getAttribute("userObj");
        System.out.println(obj.toString());
        User user = (User)(obj);
        try{
            loginService.loginUser(user);

            String token = jwtManager.generateToken(user);
            Cookie cookie = new Cookie("token", token);
            resp.addCookie(cookie);
            System.out.println(jwtManager.getUserRole(token));
//            HttpSession session = req.getSession();
//            session.setAttribute("user", user);
//            session.setMaxInactiveInterval(60*4);
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"status\":\"success\",\"message\":\"Login successful\"}");
        }catch (UsernamePasswordError e){
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"status\":\"failure\",\"message\":\""+e.getMessage()+"\"}");
        }
    }
}
