package com.cyberguard.model;

/** Interface: anything that can be looked up and tracked by a public Case ID. */
public interface Trackable {
    String getCaseId();
    CaseStatus getStatus();
}
