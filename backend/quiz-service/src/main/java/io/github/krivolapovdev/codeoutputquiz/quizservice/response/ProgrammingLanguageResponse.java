package io.github.krivolapovdev.codeoutputquiz.quizservice.response;

import org.springframework.lang.NonNull;

public record ProgrammingLanguageResponse(@NonNull String name, @NonNull String displayName) {}
