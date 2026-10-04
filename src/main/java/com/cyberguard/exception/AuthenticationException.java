package com.cyberguard.exception;

public class AuthenticationException extends AppException {
    public AuthenticationException() { super("Invalid email or password."); }
    public AuthenticationException(String message) { super(message); }
}
