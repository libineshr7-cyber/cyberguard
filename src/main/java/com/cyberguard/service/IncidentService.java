package com.cyberguard.service;

import com.cyberguard.dao.*;
import com.cyberguard.exception.*;
import com.cyberguard.model.*;
import com.cyberguard.util.IncidentFactory;
import com.cyberguard.util.ValidationUtil;
import java.util.*;
import java.util.regex.Pattern;

/** Business rules for reporting, tracking and managing cases. */
public class IncidentService {
    private static final Pattern CASE_ID = Pattern.compile("^CYB-\\d{4}-\\d{5}$");
    private static final List<String> PRIORITIES = List.of("Low", "Medium", "High");

    private final IncidentDAO incidentDAO = new IncidentDAO();
    private final EvidenceDAO evidenceDAO = new EvidenceDAO();
    private final InvestigationDAO investigationDAO = new InvestigationDAO();
    private final UserDAO userDAO = new UserDAO();

    public String report(User user, String name, String email, String phone, String type, String date, String description,
                         String url, String loss, String additional, List<Evidence> files) throws AppException {
        Incident inc = IncidentFactory.create(type);           // polymorphism: concrete subclass chosen by type
        inc.setIncidentType(type);
        inc.setReporterName(ValidationUtil.requireText(name, "Full name", 2, 100));
        inc.setReporterEmail(ValidationUtil.email(email));
        inc.setReporterPhone(ValidationUtil.phone(phone));
        inc.setIncidentDate(ValidationUtil.incidentDate(date));
        inc.setDescription(ValidationUtil.requireText(description, "Description", 20, 5000));
        inc.setSuspiciousUrl(ValidationUtil.optionalUrl(url));
        inc.setFinancialLoss(ValidationUtil.money(loss));
        inc.setAdditionalInfo(ValidationUtil.optionalText(additional, "Additional information", 2000));
        inc.setUserId(user == null ? null : user.getUserId());
        inc.setPriority(inc.assessPriority());                 // polymorphism: each subclass applies its own rules
        return incidentDAO.create(inc, files);
    }

    /** Public tracking: only non-sensitive data + investigator updates marked as public. */
    public Incident getPublicCase(String rawCaseId) throws AppException {
        Incident i = load(rawCaseId);
        i.addAllInvestigations(investigationDAO.findByIncident(i.getIncidentId(), true));
        return i;
    }

    public Incident getForUser(User user, String rawCaseId) throws AppException {
        Incident i = load(rawCaseId);
        if (i.getUserId() == null || i.getUserId() != user.getUserId()) throw new AccessDeniedException();
        i.addAllEvidence(evidenceDAO.findByIncident(i.getIncidentId()));
        i.addAllInvestigations(investigationDAO.findByIncident(i.getIncidentId(), true));
        i.addAllHistory(incidentDAO.findHistory(i.getIncidentId()));
        return i;
    }

    public List<Incident> listForUser(int userId) throws AppException { return incidentDAO.findByUser(userId); }

    // ---------------- staff ----------------
    public Incident getForStaff(int id) throws AppException {
        Incident i = incidentDAO.findById(id);
        if (i == null) throw new CaseNotFoundException("#" + id);
        i.addAllEvidence(evidenceDAO.findByIncident(id));
        i.addAllInvestigations(investigationDAO.findByIncident(id, false));
        i.addAllHistory(incidentDAO.findHistory(id));
        return i;
    }

    public List<Incident> search(String q, String type, String status, String date) throws AppException {
        if (type != null && !type.isBlank() && !IncidentFactory.TYPES.contains(type)) throw new ValidationException("Invalid category filter.");
        if (status != null && !status.isBlank()) CaseStatus.fromString(status);
        if (date != null && !date.isBlank() && !date.matches("\\d{4}-\\d{2}-\\d{2}")) throw new ValidationException("Invalid date filter.");
        return incidentDAO.search(q, type, status, date);
    }

    public Map<String, Integer> stats() throws AppException {
        Map<CaseStatus, Integer> m = incidentDAO.countByStatus();
        Map<String, Integer> s = new LinkedHashMap<>();
        s.put("total", m.values().stream().mapToInt(Integer::intValue).sum());
        s.put("newCases", m.get(CaseStatus.SUBMITTED));
        s.put("underReview", m.get(CaseStatus.UNDER_REVIEW));
        s.put("investigating", m.get(CaseStatus.ASSIGNED) + m.get(CaseStatus.INVESTIGATION));
        s.put("resolved", m.get(CaseStatus.RESOLVED));
        s.put("closed", m.get(CaseStatus.CLOSED));
        return s;
    }

    /**
     * @param action "save", "resolve" or "close". Parameters that are null keep the current value.
     */
    public void updateCase(User staff, int incidentId, String action, String statusStr, String assignedStr,
                           String priorityStr, String note, boolean publicNote) throws AppException {
        Incident cur = incidentDAO.findById(incidentId);
        if (cur == null) throw new CaseNotFoundException("#" + incidentId);

        CaseStatus status;
        if ("resolve".equals(action)) status = CaseStatus.RESOLVED;
        else if ("close".equals(action)) status = CaseStatus.CLOSED;
        else status = (statusStr == null || statusStr.isBlank()) ? cur.getStatus() : CaseStatus.fromString(statusStr);

        Integer assigned = cur.getAssignedTo();
        if (assignedStr != null) {
            if (assignedStr.isBlank()) assigned = null;
            else {
                try {
                    User inv = userDAO.findById(Integer.parseInt(assignedStr.trim()));
                    if (inv == null || !inv.isStaff()) throw new ValidationException("Selected investigator is not valid.");
                    assigned = inv.getUserId();
                } catch (NumberFormatException e) {
                    throw new ValidationException("Selected investigator is not valid.");
                }
            }
        }
        // assigning a case that is still new/under review moves it to ASSIGNED automatically
        if (assigned != null && status.getStep() < CaseStatus.ASSIGNED.getStep()) status = CaseStatus.ASSIGNED;

        String priority = cur.getPriority();
        if (priorityStr != null && !priorityStr.isBlank()) {
            if (!PRIORITIES.contains(priorityStr)) throw new ValidationException("Invalid priority.");
            priority = priorityStr;
        }
        String cleanNote = ValidationUtil.optionalText(note, "Investigation note", 2000);

        incidentDAO.updateCase(incidentId, status, assigned, priority, staff.getUserId());
        if (cleanNote != null)
            investigationDAO.add(new Investigation(incidentId, staff.getUserId(), cleanNote, publicNote));
    }

    private Incident load(String raw) throws AppException {
        String id = raw == null ? "" : raw.trim().toUpperCase();
        if (id.isEmpty()) throw new ValidationException("Please enter a Case ID.");
        if (!CASE_ID.matcher(id).matches()) throw new ValidationException("Invalid Case ID format. It looks like CYB-2026-00001.");
        Incident i = incidentDAO.findByCaseId(id);
        if (i == null) throw new CaseNotFoundException(id);
        return i;
    }
}
