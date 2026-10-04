package com.cyberguard.controller;

import com.cyberguard.exception.AppException;
import com.cyberguard.service.IncidentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/track")
public class TrackServlet extends BaseServlet {
    private final IncidentService service = new IncidentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("caseId");
        if (id != null) {
            try {
                req.setAttribute("incident", service.getPublicCase(id));
            } catch (AppException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        forward(req, resp, "track");
    }
}
