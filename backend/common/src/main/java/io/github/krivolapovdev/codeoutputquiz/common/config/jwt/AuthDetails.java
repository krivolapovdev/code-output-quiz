package io.github.krivolapovdev.codeoutputquiz.common.config.jwt;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record AuthDetails(@NonNull UUID userId) {}
