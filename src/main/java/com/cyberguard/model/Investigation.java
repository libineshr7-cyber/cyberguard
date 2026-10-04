package com.cyberguard.model;

import java.sql.Timestamp;

/** One investigation note. Public notes are visible to the reporter, internal notes are staff-only. */
public class Investigation {
    private int investigationId;
    private int incidentId;
    private int investigatorId;
    private String investigatorName;
    private String notes;
    private boolean publicNote;
    private Timestamp createdAt;

    public Investigation() { }
    public Investigation(int incidentId, int investigatorId, String notes, boolean publicNote) {
        this.incidentId = incidentId; this.investigatorId = investigatorId;
        this.notes = notes; this.publicNote = publicNote;
    }
    public int getInvestigationId() { return investigationId; }
    public void setInvestigationId(int v) { investigationId = v; }
    public int getIncidentId() { return incidentId; }
    public void setIncidentId(int v) { incidentId = v; }
    public int getInvestigatorId() { return investigatorId; }
    public void setInvestigatorId(int v) { investigatorId = v; }
    public String getInvestigatorName() { return investigatorName; }
    public void setInvestigatorName(String v) { investigatorName = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { notes = v; }
    public boolean isPublicNote() { return publicNote; }
    public void setPublicNote(boolean v) { publicNote = v; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp v) { createdAt = v; }
}
