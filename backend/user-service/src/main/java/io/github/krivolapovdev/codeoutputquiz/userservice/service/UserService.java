package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.userservice.config.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.userservice.response.UserResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  public Mono<UserResponse> getCurrentUser(Authentication authentication) {
    log.info("Fetching current user: {}", authentication.getName());
    String email = authentication.getName();
    UUID userId = ((AuthDetails) authentication.getDetails()).userId();
    return Mono.just(new UserResponse(userId, email));
  }
}
