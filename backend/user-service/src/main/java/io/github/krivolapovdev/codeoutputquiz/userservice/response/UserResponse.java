package io.github.krivolapovdev.codeoutputquiz.userservice.response;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record UserResponse(@NonNull UUID userId, @NonNull String email) {}
