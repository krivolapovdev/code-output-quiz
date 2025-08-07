package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import java.util.UUID;
import org.springframework.lang.NonNull;

public record AuthPrincipal(@NonNull UUID id, @NonNull String email, @NonNull UserRole role) {}
