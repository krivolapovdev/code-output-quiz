package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import io.github.krivolapovdev.codeoutputquiz.userservice.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  public Mono<UserResponse> getCurrentUser(@NonNull Authentication authentication) {
    log.info("Fetching current user: {}", authentication.getName());
    AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
    UserResponse userResponse = new UserResponse(authPrincipal.id(), authPrincipal.email());
    return Mono.just(userResponse);
  }
}
