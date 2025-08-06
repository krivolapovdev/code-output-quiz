package io.github.krivolapovdev.codeoutputquiz.authservice.mapper;

import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthRequestMapper {
  public @NonNull Authentication toAuthentication(@NonNull AuthRequest request) {
    return new UsernamePasswordAuthenticationToken(request.email(), request.password());
  }
}
