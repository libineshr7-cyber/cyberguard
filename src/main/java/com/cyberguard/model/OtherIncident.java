package com.cyberguard.model;

import java.sql.Date;

public class OtherIncident extends Incident {
    public OtherIncident() { }
    public OtherIncident(String type, String description, Date incidentDate) { super(type, description, incidentDate); }

    @Override protected int getBaseRiskScore() { return 30; }

    @Override public String getCategoryGuidance() {
        return "Keep screenshots and messages as evidence, avoid engaging with the offender and block or report the account on the platform where it happened.";
    }
}
