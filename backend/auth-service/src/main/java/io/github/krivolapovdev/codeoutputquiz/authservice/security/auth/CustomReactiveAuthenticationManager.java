package io.github.krivolapovdev.codeoutputquiz.authservice.security.auth;

import io.github.krivolapovdev.codeoutputquiz.authservice.service.UserService;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Override
  public Mono<Authentication> authenticate(@NonNull Authentication authentication) {
    log.info("Authenticating user: {}", authentication.getName());

    String email = authentication.getName();
    String rawPassword = authentication.getCredentials().toString();

    return userService
        .getUserByEmail(email)
        .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
        .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid password")))
        .map(user -> new AuthPrincipal(user.getId(), user.getEmail(), user.getRole()))
        .map(
            authPrincipal ->
                new UsernamePasswordAuthenticationToken(
                    authPrincipal,
                    null,
                    AuthorityUtils.createAuthorityList(authPrincipal.role().name())));
  }
}
