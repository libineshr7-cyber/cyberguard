package com.cyberguard.model;

import java.sql.Date;

public class PhishingIncident extends Incident {
    public PhishingIncident() { }
    public PhishingIncident(String type, String description, Date incidentDate) { super(type, description, incidentDate); }

    @Override protected int getBaseRiskScore() { return 40; }

    @Override public String getCategoryGuidance() {
        return "Do not click any further links or open attachments. Change the password of any account you entered on the page, turn on two-factor authentication and report the message to your email provider.";
    }
}
