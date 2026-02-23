package com.systems.user.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("AUTH_001", "User not found"),
    INVALID_CREDENTIALS("AUTH_002", "Invalid credentials"),
    USER_ALREADY_EXISTS("AUTH_003", "User already exists"),
    ACCESS_DENIED("AUTH_004", "Access Denied"),
    INVALID_REQUEST("AUTH_005", "Invalid Request"),
    INTERNAL_ERROR("AUTH_999", "Internal server error");
    
    private final String code;
    private final String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
