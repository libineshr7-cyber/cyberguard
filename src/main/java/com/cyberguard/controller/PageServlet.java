package com.cyberguard.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Static pages: Home, About, Contact. */
@WebServlet({"/home", "/about", "/contact"})
public class PageServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forward(req, resp, req.getServletPath().substring(1));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Contact form: demo only - nothing is stored or e-mailed.
        flash(req, "flashSuccess", "Thank you! Your message was received (demo form - messages are not stored).");
        redirect(req, resp, "/contact");
    }
}
