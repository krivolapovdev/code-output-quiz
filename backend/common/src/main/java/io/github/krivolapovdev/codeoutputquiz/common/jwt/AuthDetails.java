package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import java.util.UUID;
import org.springframework.lang.NonNull;

public record AuthDetails(@NonNull UUID userId) {}
