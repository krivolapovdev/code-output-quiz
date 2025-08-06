package io.github.krivolapovdev.codeoutputquiz.authservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.exception.EmailAlreadyTakenException;
import io.github.krivolapovdev.codeoutputquiz.authservice.factory.AuthResponseEntityFactory;
import io.github.krivolapovdev.codeoutputquiz.authservice.mapper.AuthRequestMapper;
import io.github.krivolapovdev.codeoutputquiz.authservice.model.TokenPair;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import io.github.krivolapovdev.codeoutputquiz.authservice.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  @Mock private CookieService cookieService;
  @Mock private UserService userService;
  @Mock private TokenService tokenService;
  @Mock private ReactiveAuthenticationManager reactiveAuthenticationManager;
  @Mock private AuthResponseEntityFactory authResponseFactory;
  @Mock private AuthRequestMapper authRequestMapper;
  @Mock private Authentication authentication;
  @InjectMocks private AuthService authService;

  @Test
  void shouldRegisterUserSuccessfully() {
    AuthRequest request = new AuthRequest("new@example.com", "pass");
    TokenPair tokenPair = new TokenPair("access", "refresh");
    ResponseEntity<AuthResponse> expected =
        ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse());

    User dummyUser = new User(request.email(), "hashed-password");
    dummyUser.setRole(io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole.USER);

    when(userService.saveUser(request)).thenReturn(Mono.just(dummyUser));
    when(authRequestMapper.toAuthentication(request)).thenReturn(authentication);
    when(reactiveAuthenticationManager.authenticate(authentication))
        .thenReturn(Mono.just(authentication));
    when(tokenService.generateTokens(authentication)).thenReturn(tokenPair);
    when(authResponseFactory.create("access", "refresh", HttpStatus.CREATED)).thenReturn(expected);

    authService
        .register(request)
        .as(StepVerifier::create)
        .assertNext(response -> assertThat(response).isSameAs(expected))
        .verifyComplete();

    verify(userService).saveUser(request);
    verify(authRequestMapper).toAuthentication(request);
    verify(reactiveAuthenticationManager).authenticate(authentication);
    verify(tokenService).generateTokens(authentication);
    verify(authResponseFactory).create("access", "refresh", HttpStatus.CREATED);
  }

  @Test
  void shouldMapDuplicateKeyExceptionToEmailAlreadyTaken() {
    AuthRequest request = new AuthRequest("duplicate@example.com", "pass");

    when(userService.saveUser(request)).thenReturn(Mono.error(new DuplicateKeyException("DUP")));

    authService
        .register(request)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            error -> {
              assertThat(error).isInstanceOf(EmailAlreadyTakenException.class);
              assertThat(error.getMessage()).isEqualTo("Email already exists");
            })
        .verify();

    verify(userService).saveUser(request);
    verifyNoInteractions(
        authRequestMapper, reactiveAuthenticationManager, tokenService, authResponseFactory);
  }

  @Test
  void shouldLoginUserSuccessfully() {
    AuthRequest request = new AuthRequest("user@example.com", "pass");
    TokenPair tokenPair = new TokenPair("access", "refresh");
    ResponseEntity<AuthResponse> expected = ResponseEntity.ok(new AuthResponse());

    when(authRequestMapper.toAuthentication(request)).thenReturn(authentication);
    when(reactiveAuthenticationManager.authenticate(authentication))
        .thenReturn(Mono.just(authentication));
    when(tokenService.generateTokens(authentication)).thenReturn(tokenPair);
    when(authResponseFactory.create("access", "refresh", HttpStatus.OK)).thenReturn(expected);

    authService
        .login(request)
        .as(StepVerifier::create)
        .assertNext(response -> assertThat(response).isSameAs(expected))
        .verifyComplete();

    verify(authRequestMapper).toAuthentication(request);
    verify(reactiveAuthenticationManager).authenticate(authentication);
    verify(tokenService).generateTokens(authentication);
    verify(authResponseFactory).create("access", "refresh", HttpStatus.OK);
  }

  @Test
  void shouldLogoutUser() {
    ResponseCookie accessCookie = ResponseCookie.from("Access", "").build();
    ResponseCookie refreshCookie = ResponseCookie.from("Refresh", "").build();

    when(cookieService.clearAccessTokenCookie()).thenReturn(accessCookie);
    when(cookieService.clearRefreshTokenCookie()).thenReturn(refreshCookie);

    authService
        .logout()
        .as(StepVerifier::create)
        .assertNext(
            response -> {
              assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
              assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE))
                  .containsExactlyInAnyOrder(accessCookie.toString(), refreshCookie.toString());
            })
        .verifyComplete();

    verify(cookieService).clearAccessTokenCookie();
    verify(cookieService).clearRefreshTokenCookie();
  }
}
