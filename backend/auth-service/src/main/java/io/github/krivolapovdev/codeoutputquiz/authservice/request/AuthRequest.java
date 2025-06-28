package io.github.krivolapovdev.codeoutputquiz.authservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(
    @NotBlank(message = "Email must not be blank")
        @Email(message = "Email should be valid")
        @Pattern(
            regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Email format is invalid")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String email,
    @NotBlank(message = "Password must not be blank")
        @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
        String password) {}
