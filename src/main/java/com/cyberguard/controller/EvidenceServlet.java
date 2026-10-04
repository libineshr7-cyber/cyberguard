package com.cyberguard.controller;

import com.cyberguard.dao.EvidenceDAO;
import com.cyberguard.dao.IncidentDAO;
import com.cyberguard.exception.*;
import com.cyberguard.model.Evidence;
import com.cyberguard.model.Incident;
import com.cyberguard.model.User;
import com.cyberguard.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Downloads evidence. Only staff or the owner of the case may download; files are always sent as attachments. */
@WebServlet("/evidence")
public class EvidenceServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Evidence e = new EvidenceDAO().findById(intParam(req, "id"));
            if (e == null) throw new CaseNotFoundException("evidence");
            Incident inc = new IncidentDAO().findById(e.getIncidentId());
            User u = currentUser(req);
            boolean owner = inc != null && inc.getUserId() != null && inc.getUserId() == u.getUserId();
            if (!u.isStaff() && !owner) throw new AccessDeniedException();
            Path p = FileUtil.resolve(e.getFilePath());
            if (!Files.exists(p)) throw new CaseNotFoundException("evidence file");
            resp.setContentType(e.getFileType());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + e.getFileName() + "\"");
            resp.setHeader("X-Content-Type-Options", "nosniff");
            resp.setContentLengthLong(Files.size(p));
            Files.copy(p, resp.getOutputStream());
        } catch (AppException ex) {
            showError(req, resp, ex);
        }
    }
}
