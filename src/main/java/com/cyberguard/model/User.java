package com.cyberguard.model;

import java.sql.Timestamp;

/** Abstract base class for every account type (abstraction + encapsulation). */
public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private Timestamp createdAt;

    protected User() { }

    protected User(String name, String email, String phone) {
        this.name = name; this.email = email; this.phone = phone;
    }

    protected User(int userId, String name, String email, String passwordHash, String phone, Timestamp createdAt) {
        this(name, email, phone);
        this.userId = userId; this.passwordHash = passwordHash; this.createdAt = createdAt;
    }

    /** Role stored in the database (CITIZEN / INVESTIGATOR / ADMIN). */
    public abstract String getRole();
    /** Where this kind of user lands after login. */
    public abstract String getDashboardPath();
    public boolean isStaff() { return false; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override public String toString() { return getRole() + "[" + name + " <" + email + ">]"; }
}
