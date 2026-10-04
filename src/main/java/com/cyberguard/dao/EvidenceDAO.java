package com.cyberguard.dao;

import com.cyberguard.exception.AppException;
import com.cyberguard.exception.DatabaseException;
import com.cyberguard.model.Evidence;
import com.cyberguard.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvidenceDAO {
    /** Used inside the report transaction, so it accepts an open connection. */
    static void insert(Connection c, int incidentId, Evidence e) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO evidence (incident_id, file_name, file_type, file_path) VALUES (?,?,?,?)")) {
            ps.setInt(1, incidentId);
            ps.setString(2, e.getFileName());
            ps.setString(3, e.getFileType());
            ps.setString(4, e.getFilePath());
            ps.executeUpdate();
        }
    }

    public List<Evidence> findByIncident(int incidentId) throws AppException {
        String sql = "SELECT * FROM evidence WHERE incident_id = ? ORDER BY uploaded_at, evidence_id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, incidentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Evidence> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Could not load evidence.", ex);
        }
    }

    public Evidence findById(int id) throws AppException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM evidence WHERE evidence_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException ex) {
            throw new DatabaseException("Could not load evidence.", ex);
        }
    }

    private Evidence map(ResultSet rs) throws SQLException {
        Evidence e = new Evidence(rs.getString("file_name"), rs.getString("file_type"), rs.getString("file_path"));
        e.setEvidenceId(rs.getInt("evidence_id"));
        e.setIncidentId(rs.getInt("incident_id"));
        e.setUploadedAt(rs.getTimestamp("uploaded_at"));
        return e;
    }
}
