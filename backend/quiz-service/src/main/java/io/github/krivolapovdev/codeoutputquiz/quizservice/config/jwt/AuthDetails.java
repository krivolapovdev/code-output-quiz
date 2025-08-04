package io.github.krivolapovdev.codeoutputquiz.quizservice.config.jwt;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record AuthDetails(@NonNull UUID userId) {}
