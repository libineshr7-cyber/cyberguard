package com.cyberguard.model;

import java.sql.Date;

public class FraudIncident extends Incident {
    public FraudIncident() { }
    public FraudIncident(String type, String description, Date incidentDate) { super(type, description, incidentDate); }

    @Override protected int getBaseRiskScore() { return 50; }

    @Override public String getCategoryGuidance() {
        return "Contact your bank or payment provider immediately, keep transaction receipts and do not send any further money.";
    }

    /** Overrides the template method: large losses are always escalated to High priority. */
    @Override public String assessPriority() {
        if (getFinancialLoss() != null && getFinancialLoss().compareTo(new java.math.BigDecimal("50000")) >= 0) return "High";
        return super.assessPriority();
    }
}
