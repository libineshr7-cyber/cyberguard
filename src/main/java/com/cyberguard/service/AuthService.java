package com.cyberguard.service;

import com.cyberguard.dao.UserDAO;
import com.cyberguard.exception.*;
import com.cyberguard.model.Citizen;
import com.cyberguard.model.User;
import com.cyberguard.util.LoginThrottle;
import com.cyberguard.util.PasswordUtil;
import com.cyberguard.util.ValidationUtil;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public Citizen register(String name, String email, String phone, String password, String confirm) throws AppException {
        String n = ValidationUtil.requireText(name, "Full name", 2, 100);
        String e = ValidationUtil.email(email);
        String p = ValidationUtil.phone(phone);
        ValidationUtil.password(password);
        if (!password.equals(confirm)) throw new ValidationException("Passwords do not match.");
        Citizen c = new Citizen(n, e, p);
        c.setPasswordHash(PasswordUtil.hash(password));
        c.setUserId(userDAO.insert(c));
        return c;
    }

    /** @param staffLogin true for the admin/investigator login page, false for the citizen login page */
    public User login(String email, String password, boolean staffLogin) throws AppException {
        if (email == null || email.isBlank() || password == null || password.isEmpty())
            throw new ValidationException("Email and password are required.");
        String key = email.trim().toLowerCase();
        if (LoginThrottle.isLocked(key))
            throw new AuthenticationException("Too many failed attempts. Please wait 5 minutes and try again.");
        User u = userDAO.findByEmail(key);
        if (u == null || !PasswordUtil.verify(password, u.getPasswordHash()) || u.isStaff() != staffLogin) {
            LoginThrottle.recordFailure(key);
            throw new AuthenticationException();
        }
        LoginThrottle.reset(key);
        return u;
    }
}
