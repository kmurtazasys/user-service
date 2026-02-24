package com.systems.user.dto;

public record LoginResponse(String accessToken, String tokeType, String expiresIn) {
}
