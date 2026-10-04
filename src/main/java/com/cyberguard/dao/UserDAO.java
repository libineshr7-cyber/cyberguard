package com.cyberguard.dao;

import com.cyberguard.exception.*;
import com.cyberguard.model.*;
import com.cyberguard.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO pattern: all SQL for the users table lives here (PreparedStatement only). */
public class UserDAO {
    private static final String COLS = "user_id, name, email, password_hash, phone, role, created_at";

    public int insert(User u) throws AppException {
        String sql = "INSERT INTO users (name, email, password_hash, phone, role) VALUES (?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getRole());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next() ? rs.getInt(1) : 0; }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateEmailException();
        } catch (SQLException e) {
            throw new DatabaseException("Could not create the account. Please try again.", e);
        }
    }

    public User findByEmail(String email) throws AppException { return one("WHERE email = ?", email); }

    public User findById(int id) throws AppException { return one("WHERE user_id = ?", id); }

    public List<User> findStaff() throws AppException {
        String sql = "SELECT " + COLS + " FROM users WHERE role IN ('INVESTIGATOR','ADMIN') ORDER BY name";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<User> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Could not load investigators.", e);
        }
    }

    private User one(String where, Object param) throws AppException {
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT " + COLS + " FROM users " + where)) {
            ps.setObject(1, param);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (SQLException e) {
            throw new DatabaseException("Could not load the account.", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String name = rs.getString("name"), email = rs.getString("email"), hash = rs.getString("password_hash"), phone = rs.getString("phone");
        Timestamp created = rs.getTimestamp("created_at");
        switch (rs.getString("role")) {
            case "ADMIN": return new Admin(id, name, email, hash, phone, created);
            case "INVESTIGATOR": return new Investigator(id, name, email, hash, phone, created);
            default: return new Citizen(id, name, email, hash, phone, created);
        }
    }
}
