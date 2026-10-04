package com.cyberguard.exception;

public class CaseNotFoundException extends AppException {
    public CaseNotFoundException(String caseId) {
        super("No case was found for \"" + caseId + "\". Please check the Case ID and try again.");
    }
}
