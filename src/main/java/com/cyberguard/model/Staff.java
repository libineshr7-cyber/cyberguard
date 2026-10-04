package com.cyberguard.model;

import java.sql.Timestamp;

/** Abstract parent of Investigator and Admin. */
public abstract class Staff extends User {
    protected Staff() { }
    protected Staff(String name, String email, String phone) { super(name, email, phone); }
    protected Staff(int id, String name, String email, String hash, String phone, Timestamp created) {
        super(id, name, email, hash, phone, created);
    }
    public abstract String getDepartment();
    @Override public boolean isStaff() { return true; }
    @Override public String getDashboardPath() { return "/admin/dashboard"; }
}
