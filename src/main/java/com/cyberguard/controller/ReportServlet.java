package com.cyberguard.controller;

import com.cyberguard.exception.*;
import com.cyberguard.model.Evidence;
import com.cyberguard.service.IncidentService;
import com.cyberguard.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** /report shows and processes the incident form; /report-success shows the Case ID. */
@WebServlet({"/report", "/report-success"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5L * 1024 * 1024, maxRequestSize = 16L * 1024 * 1024)
public class ReportServlet extends BaseServlet {
    private final IncidentService service = new IncidentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/report-success".equals(req.getServletPath())) {
            HttpSession s = req.getSession(false);
            Object id = s == null ? null : s.getAttribute("lastCaseId");
            if (id == null) { redirect(req, resp, "/report"); return; }
            s.removeAttribute("lastCaseId");
            req.setAttribute("caseId", id);
            forward(req, resp, "report-success");
            return;
        }
        forward(req, resp, "report");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<Evidence> stored = new ArrayList<>();
        try {
            List<Part> parts = new ArrayList<>();
            for (Part p : req.getParts()) {
                String n = p.getSubmittedFileName();
                if ("evidence".equals(p.getName()) && n != null && !n.isBlank() && p.getSize() > 0) parts.add(p);
            }
            if (parts.size() > 3) throw new FileValidationException("You can attach a maximum of 3 files.");
            for (Part p : parts) stored.add(FileUtil.store(p));

            String caseId = service.report(currentUser(req),
                    req.getParameter("fullName"), req.getParameter("email"), req.getParameter("phone"),
                    req.getParameter("incidentType"), req.getParameter("incidentDate"), req.getParameter("description"),
                    req.getParameter("suspiciousUrl"), req.getParameter("financialLoss"), req.getParameter("additionalInfo"), stored);
            req.getSession().setAttribute("lastCaseId", caseId);   // Post/Redirect/Get
            redirect(req, resp, "/report-success");
        } catch (AppException e) {
            FileUtil.deleteQuietly(stored);
            req.setAttribute("error", e.getMessage());
            forward(req, resp, "report");
        } catch (IllegalStateException e) {
            FileUtil.deleteQuietly(stored);
            req.setAttribute("error", "Upload too large: each file must be 5 MB or smaller (16 MB in total).");
            forward(req, resp, "report");
        }
    }
}
