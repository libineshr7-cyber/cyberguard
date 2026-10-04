package com.cyberguard.controller;

import com.cyberguard.exception.AppException;
import com.cyberguard.model.User;
import com.cyberguard.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Handles both /login (citizens) and /admin-login (staff). */
@WebServlet({"/login", "/admin-login"})
public class LoginServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    private boolean staff(HttpServletRequest req) { return "/admin-login".equals(req.getServletPath()); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = currentUser(req);
        if (u != null) { redirect(req, resp, u.getDashboardPath()); return; }
        forward(req, resp, staff(req) ? "admin-login" : "login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User u = auth.login(req.getParameter("email"), req.getParameter("password"), staff(req));
            req.getSession(true);
            req.changeSessionId();                       // prevents session fixation
            req.getSession().setAttribute("user", u);
            redirect(req, resp, u.getDashboardPath());
        } catch (AppException e) {
            req.setAttribute("error", e.getMessage());
            forward(req, resp, staff(req) ? "admin-login" : "login");
        }
    }
}
