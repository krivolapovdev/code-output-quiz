package io.github.krivolapovdev.codeoutputquiz.userservice.response;

import java.util.UUID;

public record UserResponse(UUID userId, String email) {}
