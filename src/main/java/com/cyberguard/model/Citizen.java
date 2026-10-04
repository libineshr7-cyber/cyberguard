package com.cyberguard.model;

import java.sql.Timestamp;

public class Citizen extends User {
    public Citizen() { }
    public Citizen(String name, String email, String phone) { super(name, email, phone); }
    public Citizen(int id, String name, String email, String hash, String phone, Timestamp created) {
        super(id, name, email, hash, phone, created);
    }
    @Override public String getRole() { return "CITIZEN"; }
    @Override public String getDashboardPath() { return "/user/dashboard"; }
}
