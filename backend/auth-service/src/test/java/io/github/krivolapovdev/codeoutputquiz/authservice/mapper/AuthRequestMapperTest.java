package io.github.krivolapovdev.codeoutputquiz.authservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthRequestMapperTest {
  @InjectMocks private AuthRequestMapper authRequestMapper;

  @Test
  void shouldMapAuthRequestToAuthenticationToken() {
    var email = "user@example.com";
    var password = "password";
    var request = new AuthRequest(email, password);

    Authentication auth = authRequestMapper.toAuthentication(request);

    assertThat(auth)
        .isInstanceOf(UsernamePasswordAuthenticationToken.class)
        .extracting(Authentication::getName, Authentication::getCredentials)
        .containsExactly(email, password);
  }
}
