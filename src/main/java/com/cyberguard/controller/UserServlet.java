package com.cyberguard.controller;

import com.cyberguard.exception.AppException;
import com.cyberguard.model.CaseStatus;
import com.cyberguard.model.Incident;
import com.cyberguard.model.User;
import com.cyberguard.service.IncidentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/** Citizen area: dashboard, my reports, case details. Protected by AuthFilter. */
@WebServlet({"/user/dashboard", "/user/reports", "/user/case"})
public class UserServlet extends BaseServlet {
    private final IncidentService service = new IncidentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = currentUser(req);
        String path = req.getServletPath();
        try {
            if ("/user/case".equals(path)) {
                req.setAttribute("incident", service.getForUser(u, req.getParameter("caseId")));
                forward(req, resp, "case-details");
                return;
            }
            List<Incident> list = service.listForUser(u.getUserId());
            req.setAttribute("incidents", list);
            req.setAttribute("total", list.size());
            req.setAttribute("underReview", list.stream().filter(i -> i.getStatus() == CaseStatus.SUBMITTED || i.getStatus() == CaseStatus.UNDER_REVIEW).count());
            req.setAttribute("investigating", list.stream().filter(i -> i.getStatus() == CaseStatus.ASSIGNED || i.getStatus() == CaseStatus.INVESTIGATION).count());
            req.setAttribute("resolved", list.stream().filter(i -> i.getStatus() == CaseStatus.RESOLVED || i.getStatus() == CaseStatus.CLOSED).count());
            forward(req, resp, "/user/dashboard".equals(path) ? "user-dashboard" : "my-reports");
        } catch (AppException e) {
            showError(req, resp, e);
        }
    }
}
