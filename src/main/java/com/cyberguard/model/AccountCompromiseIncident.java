package com.cyberguard.model;

import java.sql.Date;

public class AccountCompromiseIncident extends Incident {
    public AccountCompromiseIncident() { }
    public AccountCompromiseIncident(String type, String description, Date incidentDate) { super(type, description, incidentDate); }

    @Override protected int getBaseRiskScore() { return 60; }

    @Override public String getCategoryGuidance() {
        return "Reset the password from a trusted device, sign out of all sessions, enable two-factor authentication and check recovery email/phone settings for changes.";
    }
}
