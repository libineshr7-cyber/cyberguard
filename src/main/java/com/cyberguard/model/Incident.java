package com.cyberguard.model;

import com.cyberguard.exception.ValidationException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Abstract parent of all incident types.
 * Demonstrates abstraction (abstract methods), encapsulation (private fields),
 * interfaces, constructor overloading, method overloading and collections.
 */
public abstract class Incident implements PriorityAssessable, Trackable {
    private int incidentId;
    private String caseId;
    private Integer userId;               // null for guest reports
    private String reporterName, reporterEmail, reporterPhone;
    private String incidentType, description, suspiciousUrl, additionalInfo;
    private Date incidentDate;
    private BigDecimal financialLoss = BigDecimal.ZERO;
    private String priority = "Low";
    private CaseStatus status = CaseStatus.SUBMITTED;
    private Integer assignedTo;
    private String assignedToName;
    private boolean demo;
    private Timestamp createdAt, updatedAt;

    private final List<Evidence> evidenceList = new ArrayList<>();
    private final List<Investigation> investigations = new ArrayList<>();
    private final List<StatusHistory> history = new ArrayList<>();

    protected Incident() { }

    protected Incident(String incidentType, String description, Date incidentDate) {
        this.incidentType = incidentType; this.description = description; this.incidentDate = incidentDate;
    }

    /** Each incident type contributes a different base risk score (0-100). */
    protected abstract int getBaseRiskScore();

    /** Safety advice shown to the reporter; different for every incident type. */
    public abstract String getCategoryGuidance();

    /** Template method - child classes may override to apply their own rules. */
    @Override
    public String assessPriority() {
        int score = getBaseRiskScore() + lossScore();
        return score >= 75 ? "High" : score >= 50 ? "Medium" : "Low";
    }

    // ---- method overloading ----
    protected int lossScore() { return lossScore(financialLoss); }

    protected int lossScore(BigDecimal loss) {
        if (loss == null) return 0;
        if (loss.compareTo(new BigDecimal("100000")) >= 0) return 30;
        if (loss.compareTo(new BigDecimal("10000")) >= 0) return 15;
        return loss.signum() > 0 ? 5 : 0;
    }

    public void setStatus(CaseStatus status) { this.status = status; }
    public void setStatus(String status) throws ValidationException { this.status = CaseStatus.fromString(status); }

    // ---- collections ----
    public void addAllEvidence(Collection<Evidence> c) { evidenceList.addAll(c); }
    public void addAllInvestigations(Collection<Investigation> c) { investigations.addAll(c); }
    public void addAllHistory(Collection<StatusHistory> c) { history.addAll(c); }
    public List<Evidence> getEvidenceList() { return Collections.unmodifiableList(evidenceList); }
    public List<Investigation> getInvestigations() { return Collections.unmodifiableList(investigations); }
    public List<StatusHistory> getHistory() { return Collections.unmodifiableList(history); }

    // ---- getters / setters ----
    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int v) { incidentId = v; }
    @Override public String getCaseId() { return caseId; }
    public void setCaseId(String v) { caseId = v; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer v) { userId = v; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String v) { reporterName = v; }
    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String v) { reporterEmail = v; }
    public String getReporterPhone() { return reporterPhone; }
    public void setReporterPhone(String v) { reporterPhone = v; }
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String v) { incidentType = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public Date getIncidentDate() { return incidentDate; }
    public void setIncidentDate(Date v) { incidentDate = v; }
    public String getSuspiciousUrl() { return suspiciousUrl; }
    public void setSuspiciousUrl(String v) { suspiciousUrl = v; }
    public BigDecimal getFinancialLoss() { return financialLoss; }
    public void setFinancialLoss(BigDecimal v) { financialLoss = v; }
    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String v) { additionalInfo = v; }
    public String getPriority() { return priority; }
    public void setPriority(String v) { priority = v; }
    @Override public CaseStatus getStatus() { return status; }
    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer v) { assignedTo = v; }
    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String v) { assignedToName = v; }
    public boolean isDemo() { return demo; }
    public void setDemo(boolean v) { demo = v; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp v) { createdAt = v; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp v) { updatedAt = v; }

    @Override public String toString() { return getClass().getSimpleName() + "[" + caseId + ", " + incidentType + ", " + status + "]"; }
}
