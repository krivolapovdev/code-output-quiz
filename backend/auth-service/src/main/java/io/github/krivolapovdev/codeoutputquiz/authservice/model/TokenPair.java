package io.github.krivolapovdev.codeoutputquiz.authservice.model;

import org.springframework.lang.NonNull;

public record TokenPair(
    @NonNull String accessToken,
    @NonNull String refreshToken
) {}
