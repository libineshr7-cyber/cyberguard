package com.cyberguard.dao;

import com.cyberguard.exception.AppException;
import com.cyberguard.exception.DatabaseException;
import com.cyberguard.model.Investigation;
import com.cyberguard.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestigationDAO {
    public void add(Investigation n) throws AppException {
        String sql = "INSERT INTO investigations (incident_id, investigator_id, notes, is_public) VALUES (?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, n.getIncidentId());
            ps.setInt(2, n.getInvestigatorId());
            ps.setString(3, n.getNotes());
            ps.setBoolean(4, n.isPublicNote());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save the investigation note.", e);
        }
    }

    public List<Investigation> findByIncident(int incidentId, boolean publicOnly) throws AppException {
        String sql = "SELECT n.*, u.name AS investigator_name FROM investigations n JOIN users u ON u.user_id = n.investigator_id "
                + "WHERE n.incident_id = ? " + (publicOnly ? "AND n.is_public = 1 " : "")
                + "ORDER BY n.created_at DESC, n.investigation_id DESC";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, incidentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Investigation> list = new ArrayList<>();
                while (rs.next()) {
                    Investigation n = new Investigation(rs.getInt("incident_id"), rs.getInt("investigator_id"), rs.getString("notes"), rs.getBoolean("is_public"));
                    n.setInvestigationId(rs.getInt("investigation_id"));
                    n.setInvestigatorName(rs.getString("investigator_name"));
                    n.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(n);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load investigation notes.", e);
        }
    }
}
