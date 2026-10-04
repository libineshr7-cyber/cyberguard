package com.cyberguard.dao;

import com.cyberguard.exception.*;
import com.cyberguard.model.*;
import com.cyberguard.util.DBUtil;
import com.cyberguard.util.IncidentFactory;
import java.sql.*;
import java.time.Year;
import java.util.*;

public class IncidentDAO {
    private static final String SELECT =
            "SELECT i.*, a.name AS assigned_name FROM incidents i LEFT JOIN users a ON a.user_id = i.assigned_to ";

    /** Inserts the incident, generates CYB-YYYY-NNNNN, writes first history row and evidence rows in ONE transaction. */
    public String create(Incident inc, List<Evidence> files) throws AppException {
        String sql = "INSERT INTO incidents (case_id, user_id, reporter_name, reporter_email, reporter_phone, incident_type, description, "
                + "incident_date, suspicious_url, financial_loss, additional_info, priority, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "TMP-" + UUID.randomUUID().toString().replace("-", ""));
                    if (inc.getUserId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, inc.getUserId());
                    ps.setString(3, inc.getReporterName());
                    ps.setString(4, inc.getReporterEmail());
                    ps.setString(5, inc.getReporterPhone());
                    ps.setString(6, inc.getIncidentType());
                    ps.setString(7, inc.getDescription());
                    ps.setDate(8, inc.getIncidentDate());
                    ps.setString(9, inc.getSuspiciousUrl());
                    ps.setBigDecimal(10, inc.getFinancialLoss());
                    ps.setString(11, inc.getAdditionalInfo());
                    ps.setString(12, inc.getPriority());
                    ps.setString(13, inc.getStatus().name());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); id = rs.getInt(1); }
                }
                String caseId = String.format("CYB-%d-%05d", Year.now().getValue(), id);
                try (PreparedStatement ps = c.prepareStatement("UPDATE incidents SET case_id = ? WHERE incident_id = ?")) {
                    ps.setString(1, caseId);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO case_status_history (incident_id, old_status, new_status, updated_by) VALUES (?, NULL, 'SUBMITTED', NULL)")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                for (Evidence e : files) EvidenceDAO.insert(c, id, e);
                c.commit();
                return caseId;
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not save your report. Please try again.", e);
        }
    }

    public Incident findByCaseId(String caseId) throws AppException {
        List<Incident> l = query("WHERE i.case_id = ?", Collections.singletonList(caseId));
        return l.isEmpty() ? null : l.get(0);
    }

    public Incident findById(int id) throws AppException {
        List<Incident> l = query("WHERE i.incident_id = ?", Collections.singletonList(id));
        return l.isEmpty() ? null : l.get(0);
    }

    public List<Incident> findByUser(int userId) throws AppException {
        return query("WHERE i.user_id = ? ORDER BY i.created_at DESC, i.incident_id DESC", Collections.singletonList(userId));
    }

    public List<Incident> search(String caseId, String type, String status, String date) throws AppException {
        StringBuilder w = new StringBuilder("WHERE 1=1");
        List<Object> p = new ArrayList<>();
        if (caseId != null && !caseId.isBlank()) { w.append(" AND i.case_id LIKE ?"); p.add("%" + caseId.trim().toUpperCase() + "%"); }
        if (type != null && !type.isBlank()) { w.append(" AND i.incident_type = ?"); p.add(type); }
        if (status != null && !status.isBlank()) { w.append(" AND i.status = ?"); p.add(status); }
        if (date != null && !date.isBlank()) { w.append(" AND DATE(i.created_at) = ?"); p.add(date); }
        w.append(" ORDER BY i.created_at DESC, i.incident_id DESC");
        return query(w.toString(), p);
    }

    public Map<CaseStatus, Integer> countByStatus() throws AppException {
        Map<CaseStatus, Integer> m = new EnumMap<>(CaseStatus.class);
        for (CaseStatus s : CaseStatus.values()) m.put(s, 0);
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT status, COUNT(*) AS n FROM incidents GROUP BY status");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) m.put(CaseStatus.valueOf(rs.getString("status")), rs.getInt("n"));
            return m;
        } catch (SQLException e) {
            throw new DatabaseException("Could not load dashboard statistics.", e);
        }
    }

    public List<StatusHistory> findHistory(int incidentId) throws AppException {
        String sql = "SELECT h.old_status, h.new_status, h.updated_at, u.name FROM case_status_history h "
                + "LEFT JOIN users u ON u.user_id = h.updated_by WHERE h.incident_id = ? ORDER BY h.updated_at, h.history_id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, incidentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<StatusHistory> list = new ArrayList<>();
                while (rs.next()) {
                    String by = rs.getString("name");
                    list.add(new StatusHistory(rs.getString("old_status"), rs.getString("new_status"), by == null ? "System" : by, rs.getTimestamp("updated_at")));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load status history.", e);
        }
    }

    /** Updates status/assignee/priority and records a history row when the status changed (transactional). */
    public void updateCase(int incidentId, CaseStatus status, Integer assignedTo, String priority, int staffId) throws AppException {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                String old;
                try (PreparedStatement ps = c.prepareStatement("SELECT status FROM incidents WHERE incident_id = ? FOR UPDATE")) {
                    ps.setInt(1, incidentId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new CaseNotFoundException("#" + incidentId);
                        old = rs.getString(1);
                    }
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE incidents SET status = ?, assigned_to = ?, priority = ?, updated_at = CURRENT_TIMESTAMP WHERE incident_id = ?")) {
                    ps.setString(1, status.name());
                    if (assignedTo == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, assignedTo);
                    ps.setString(3, priority);
                    ps.setInt(4, incidentId);
                    ps.executeUpdate();
                }
                if (!old.equals(status.name())) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO case_status_history (incident_id, old_status, new_status, updated_by) VALUES (?,?,?,?)")) {
                        ps.setInt(1, incidentId);
                        ps.setString(2, old);
                        ps.setString(3, status.name());
                        ps.setInt(4, staffId);
                        ps.executeUpdate();
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException | AppException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not update the case. Please try again.", e);
        }
    }

    private List<Incident> query(String tail, List<?> params) throws AppException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT + tail)) {
            for (int k = 0; k < params.size(); k++) ps.setObject(k + 1, params.get(k));
            try (ResultSet rs = ps.executeQuery()) {
                List<Incident> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while loading cases.", e);
        }
    }

    private Incident map(ResultSet rs) throws SQLException {
        Incident i = IncidentFactory.createSafe(rs.getString("incident_type"));   // polymorphic object
        i.setIncidentId(rs.getInt("incident_id"));
        i.setCaseId(rs.getString("case_id"));
        int uid = rs.getInt("user_id");
        i.setUserId(rs.wasNull() ? null : uid);
        i.setReporterName(rs.getString("reporter_name"));
        i.setReporterEmail(rs.getString("reporter_email"));
        i.setReporterPhone(rs.getString("reporter_phone"));
        i.setIncidentType(rs.getString("incident_type"));
        i.setDescription(rs.getString("description"));
        i.setIncidentDate(rs.getDate("incident_date"));
        i.setSuspiciousUrl(rs.getString("suspicious_url"));
        i.setFinancialLoss(rs.getBigDecimal("financial_loss"));
        i.setAdditionalInfo(rs.getString("additional_info"));
        i.setPriority(rs.getString("priority"));
        i.setStatus(CaseStatus.valueOf(rs.getString("status")));
        int at = rs.getInt("assigned_to");
        i.setAssignedTo(rs.wasNull() ? null : at);
        i.setAssignedToName(rs.getString("assigned_name"));
        i.setDemo(rs.getBoolean("is_demo"));
        i.setCreatedAt(rs.getTimestamp("created_at"));
        i.setUpdatedAt(rs.getTimestamp("updated_at"));
        return i;
    }
}
