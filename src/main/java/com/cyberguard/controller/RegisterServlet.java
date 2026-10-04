package com.cyberguard.controller;

import com.cyberguard.exception.AppException;
import com.cyberguard.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp, "register");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            auth.register(req.getParameter("name"), req.getParameter("email"), req.getParameter("phone"),
                    req.getParameter("password"), req.getParameter("confirm"));
            flash(req, "flashSuccess", "Account created successfully. Please log in.");
            redirect(req, resp, "/login");
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            forward(req, resp, "register");
        }
    }
}
