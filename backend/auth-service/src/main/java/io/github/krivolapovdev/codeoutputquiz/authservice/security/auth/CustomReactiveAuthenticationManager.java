package io.github.krivolapovdev.codeoutputquiz.authservice.security.auth;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    log.info("Authenticating user: {}", authentication.getName());

    String email = authentication.getName();
    String rawPassword = authentication.getCredentials().toString();

    return userRepository
        .findByEmail(email)
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found: " + email)))
        .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
        .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid password")))
        .map(
            user -> {
              var auth =
                  new UsernamePasswordAuthenticationToken(
                      user.getEmail(),
                      null,
                      AuthorityUtils.createAuthorityList(user.getRole().name()));

              AuthDetails authDetails = new AuthDetails(user.getId());
              auth.setDetails(authDetails);

              return auth;
            });
  }
}
