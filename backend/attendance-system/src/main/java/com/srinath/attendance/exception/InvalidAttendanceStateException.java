package com.srinath.attendance.exception;

public class InvalidAttendanceStateException extends RuntimeException {
    public InvalidAttendanceStateException(String message) {
        super(message);
    }

    public InvalidAttendanceStateException(String message, Throwable cause) {
        super(message, cause);
    }
}

