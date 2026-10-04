package com.cyberguard;

import static org.junit.jupiter.api.Assertions.*;

import com.cyberguard.exception.ValidationException;
import com.cyberguard.model.*;
import com.cyberguard.util.*;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OopAndValidationTest {

    @Test void passwordsAreHashedAndVerified() {
        String h = PasswordUtil.hash("Secret@123");
        assertNotEquals("Secret@123", h);
        assertTrue(h.startsWith("pbkdf2$"));
        assertTrue(PasswordUtil.verify("Secret@123", h));
        assertFalse(PasswordUtil.verify("wrong", h));
        assertNotEquals(h, PasswordUtil.hash("Secret@123"), "salt must make every hash different");
    }

    @Test void factoryReturnsCorrectSubclass() throws Exception {
        assertInstanceOf(PhishingIncident.class, IncidentFactory.create("Phishing"));
        assertInstanceOf(FraudIncident.class, IncidentFactory.create("Online Fraud"));
        assertInstanceOf(MalwareIncident.class, IncidentFactory.create("Malware"));
        assertInstanceOf(AccountCompromiseIncident.class, IncidentFactory.create("Account Compromise"));
        assertThrows(ValidationException.class, () -> IncidentFactory.create("Bogus"));
    }

    @Test void polymorphicPriority() throws Exception {
        List<Incident> list = List.of(new PhishingIncident(), new FraudIncident(), new MalwareIncident());
        list.forEach(i -> i.setFinancialLoss(new BigDecimal("60000")));
        assertEquals("High", list.get(1).assessPriority());   // FraudIncident overrides assessPriority()
        assertEquals("Medium", list.get(2).assessPriority()); // 55 + 15
        assertEquals("Low", new PhishingIncident().assessPriority());
    }

    @Test void abstractUserHierarchy() {
        User[] users = {new Citizen(), new Investigator(), new Admin()};
        assertEquals("CITIZEN", users[0].getRole());
        assertFalse(users[0].isStaff());
        assertTrue(users[1].isStaff());
        assertEquals("/admin/dashboard", users[2].getDashboardPath());
    }

    @Test void statusOrderAndParsing() throws Exception {
        assertEquals(CaseStatus.UNDER_REVIEW, CaseStatus.fromString(" under_review "));
        assertTrue(CaseStatus.ASSIGNED.getStep() > CaseStatus.SUBMITTED.getStep());
        assertThrows(ValidationException.class, () -> CaseStatus.fromString("nope"));
    }

    @Test void validationRules() throws Exception {
        assertEquals("a@b.com", ValidationUtil.email(" A@B.com "));
        assertThrows(ValidationException.class, () -> ValidationUtil.email("not-an-email"));
        assertThrows(ValidationException.class, () -> ValidationUtil.password("short1"));
        assertThrows(ValidationException.class, () -> ValidationUtil.password("onlyletters"));
        assertThrows(ValidationException.class, () -> ValidationUtil.optionalUrl("javascript:alert(1)"));
        assertNull(ValidationUtil.optionalUrl("  "));
        assertThrows(ValidationException.class, () -> ValidationUtil.incidentDate("2999-01-01"));
        assertEquals(new BigDecimal("12.50"), ValidationUtil.money("12.5"));
        assertThrows(ValidationException.class, () -> ValidationUtil.money("-1"));
        assertThrows(ValidationException.class, () -> ValidationUtil.requireText("  ", "Name"));
    }

    @Test void loginThrottleLocksAfterFiveFailures() {
        String k = "throttle-test@example.com";
        LoginThrottle.reset(k);
        for (int i = 0; i < 5; i++) LoginThrottle.recordFailure(k);
        assertTrue(LoginThrottle.isLocked(k));
        LoginThrottle.reset(k);
        assertFalse(LoginThrottle.isLocked(k));
    }
}
