package com.cyberguard.controller;

import com.cyberguard.exception.*;
import com.cyberguard.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Shared helpers for all controllers (inheritance). */
public abstract class BaseServlet extends HttpServlet {

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String view) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
    }

    protected User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (User) s.getAttribute("user");
    }

    protected void flash(HttpServletRequest req, String key, String message) {
        req.getSession().setAttribute(key, message);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    protected int intParam(HttpServletRequest req, String name) throws ValidationException {
        try {
            return Integer.parseInt(req.getParameter(name).trim());
        } catch (RuntimeException e) {
            throw new ValidationException("Invalid request.");
        }
    }

    /** Shows a friendly error page with a suitable HTTP status. */
    protected void showError(HttpServletRequest req, HttpServletResponse resp, AppException e) throws ServletException, IOException {
        int status = (e instanceof AccessDeniedException) ? 403
                : (e instanceof CaseNotFoundException) ? 404
                : (e instanceof DatabaseException) ? 503 : 400;
        resp.setStatus(status);
        req.setAttribute("error", e.getMessage());
        forward(req, resp, "error");
    }
}
