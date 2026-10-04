package com.cyberguard.controller;

import com.cyberguard.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Session-based authentication + role-based authorization for protected URLs. */
@WebFilter(urlPatterns = {"/user/*", "/admin/*", "/evidence"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        String path = req.getServletPath();
        boolean adminArea = path.startsWith("/admin");

        if (user == null) {
            req.getSession().setAttribute("flashError", "Please log in to continue.");
            resp.sendRedirect(req.getContextPath() + (adminArea ? "/admin-login" : "/login"));
            return;
        }
        boolean wrongRole = (adminArea && !user.isStaff()) || (path.startsWith("/user") && user.isStaff());
        if (wrongRole) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("error", "Unauthorized access. You do not have permission to view this page.");
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
            return;
        }
        resp.setHeader("Cache-Control", "no-store");
        chain.doFilter(request, response);
    }
}
