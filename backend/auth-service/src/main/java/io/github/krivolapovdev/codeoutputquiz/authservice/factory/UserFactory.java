package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {
  private final PasswordEncoder passwordEncoder;

  public @NonNull User create(@NonNull AuthRequest request) {
    return new User(request.email(), passwordEncoder.encode(request.password()));
  }
}
