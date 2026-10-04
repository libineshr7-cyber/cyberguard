package com.cyberguard.util;

import com.cyberguard.exception.ValidationException;
import com.cyberguard.model.*;
import java.util.List;

/** Factory: maps the category chosen by the user to the right Incident subclass (polymorphism). */
public final class IncidentFactory {
    public static final List<String> TYPES = List.of("Phishing", "Online Fraud", "Account Compromise", "Malware",
            "Fake Website", "Cyberbullying", "Unauthorized Access", "Identity Theft", "Other");

    private IncidentFactory() { }

    public static Incident create(String type) throws ValidationException {
        if (type == null) throw new ValidationException("Please select an incident type.");
        switch (type) {
            case "Phishing": case "Fake Website": return new PhishingIncident();
            case "Online Fraud": case "Identity Theft": return new FraudIncident();
            case "Malware": return new MalwareIncident();
            case "Account Compromise": case "Unauthorized Access": return new AccountCompromiseIncident();
            case "Cyberbullying": case "Other": return new OtherIncident();
            default: throw new ValidationException("Please select a valid incident type.");
        }
    }

    /** Used when reading rows from the database: never fails. */
    public static Incident createSafe(String type) {
        try { return create(type); } catch (ValidationException e) { return new OtherIncident(); }
    }
}
