package com.systems.user.dto;

public record LoginResponse(String token, String tokeType, String expiresIn) {
}
