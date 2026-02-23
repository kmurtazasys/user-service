package com.systems.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateUserRequest(@NotBlank String password, @NotNull Set<Long> roleIds,@NotNull Boolean enabled) {}
