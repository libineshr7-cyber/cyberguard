package com.cyberguard.model;

import java.sql.Timestamp;

public class Admin extends Staff {
    public Admin() { }
    public Admin(String name, String email, String phone) { super(name, email, phone); }
    public Admin(int id, String name, String email, String hash, String phone, Timestamp created) {
        super(id, name, email, hash, phone, created);
    }
    @Override public String getRole() { return "ADMIN"; }
    @Override public String getDepartment() { return "Administration"; }
}
