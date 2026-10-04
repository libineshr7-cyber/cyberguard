package com.cyberguard.exception;

/** Base checked exception for every application-level error (inheritance + exception handling). */
public class AppException extends Exception {
    public AppException(String message) { super(message); }
    public AppException(String message, Throwable cause) { super(message, cause); }
}
