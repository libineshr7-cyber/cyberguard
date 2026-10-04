package com.cyberguard.exception;

public class FileValidationException extends AppException {
    public FileValidationException(String message) { super(message); }
    public FileValidationException(String message, Throwable cause) { super(message, cause); }
}
