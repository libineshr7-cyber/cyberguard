package com.cyberguard.model;

import java.sql.Timestamp;

public class Investigator extends Staff {
    public Investigator() { }
    public Investigator(String name, String email, String phone) { super(name, email, phone); }
    public Investigator(int id, String name, String email, String hash, String phone, Timestamp created) {
        super(id, name, email, hash, phone, created);
    }
    @Override public String getRole() { return "INVESTIGATOR"; }
    @Override public String getDepartment() { return "Cyber Incident Investigation"; }
}
