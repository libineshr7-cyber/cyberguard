package com.cyberguard.model;

import com.cyberguard.exception.ValidationException;

/** Ordered life-cycle of a case. The ordinal drives the visual timeline. */
public enum CaseStatus {
    SUBMITTED("Submitted"), UNDER_REVIEW("Under Review"), ASSIGNED("Assigned"),
    INVESTIGATION("Investigation"), RESOLVED("Resolved"), CLOSED("Closed");

    private final String label;
    CaseStatus(String label) { this.label = label; }

    public String getLabel() { return label; }
    public int getStep() { return ordinal(); }
    public String getCssClass() { return name().toLowerCase().replace('_', '-'); }

    public static CaseStatus fromString(String value) throws ValidationException {
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (RuntimeException e) {
            throw new ValidationException("Invalid case status.");
        }
    }
}
