package io.github.nellshark.codeoutputquiz.authservice.response;

import java.util.UUID;

public record RegistrationResponse(UUID id, String email) {}
