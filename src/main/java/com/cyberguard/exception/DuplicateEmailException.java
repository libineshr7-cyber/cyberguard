package com.cyberguard.exception;

public class DuplicateEmailException extends AppException {
    public DuplicateEmailException() { super("An account with this email already exists. Please log in instead."); }
}
