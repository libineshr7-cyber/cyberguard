package com.cyberguard.exception;

public class AccessDeniedException extends AppException {
    public AccessDeniedException() { super("Unauthorized access. You do not have permission to view this page."); }
}
