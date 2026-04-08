package com.fpt.assignment.dto;

public record ChangePasswordRequest(
        String email,
        String oldPassword,
        String newPassword
) {}