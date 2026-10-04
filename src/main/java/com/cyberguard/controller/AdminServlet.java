package com.cyberguard.controller;

import com.cyberguard.dao.UserDAO;
import com.cyberguard.exception.AppException;
import com.cyberguard.service.IncidentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Admin / investigator area. Protected by AuthFilter (staff only). */
@WebServlet({"/admin/dashboard", "/admin/case"})
public class AdminServlet extends BaseServlet {
    private final IncidentService service = new IncidentService();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if ("/admin/case".equals(req.getServletPath())) {
                req.setAttribute("incident", service.getForStaff(intParam(req, "id")));
                req.setAttribute("staffList", userDAO.findStaff());
                forward(req, resp, "admin-case");
                return;
            }
            req.setAttribute("stats", service.stats());
            try {
                req.setAttribute("incidents", service.search(req.getParameter("q"), req.getParameter("type"),
                        req.getParameter("status"), req.getParameter("date")));
            } catch (com.cyberguard.exception.ValidationException ve) {
                req.setAttribute("error", ve.getMessage());
                req.setAttribute("incidents", service.search(null, null, null, null));
            }
            forward(req, resp, "admin-dashboard");
        } catch (AppException e) {
            showError(req, resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String from = req.getParameter("from");
        try {
            int id = intParam(req, "id");
            String action = req.getParameter("action");
            service.updateCase(currentUser(req), id, action, req.getParameter("status"), req.getParameter("assignedTo"),
                    req.getParameter("priority"), req.getParameter("note"), req.getParameter("publicNote") != null);
            flash(req, "flashSuccess", "Case updated successfully.");
            redirect(req, resp, "dashboard".equals(from) ? "/admin/dashboard" : "/admin/case?id=" + id);
        } catch (AppException e) {
            flash(req, "flashError", e.getMessage());
            String id = req.getParameter("id");
            redirect(req, resp, "dashboard".equals(from) || id == null || !id.matches("\\d+") ? "/admin/dashboard" : "/admin/case?id=" + id);
        }
    }
}
