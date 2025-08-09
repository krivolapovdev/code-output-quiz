package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

class JwtTokenProviderTest {
  private JwtTokenProvider jwtTokenProvider;

  private static final String RAW_SECRET =
      "test-secret-should-be-long-enough-32-bytes-min-abcdefghijklmnopqrstuvwxyz";

  private SecretKey buildKey(String rawSecret) {
    String base64 = Base64.getEncoder().encodeToString(rawSecret.getBytes(StandardCharsets.UTF_8));
    return Keys.hmacShaKeyFor(base64.getBytes(StandardCharsets.UTF_8));
  }

  private Claims parseWithSameKey(String token) {
    SecretKey key = buildKey(RAW_SECRET);
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  @BeforeEach
  void setUp() {
    JwtProperties jwtProperties = Mockito.mock(JwtProperties.class);
    Mockito.when(jwtProperties.getSecretKey()).thenReturn(RAW_SECRET);
    Mockito.when(jwtProperties.getAccessTokenExpiration()).thenReturn(Duration.ofMinutes(15));
    Mockito.when(jwtProperties.getRefreshTokenExpiration()).thenReturn(Duration.ofDays(7));

    jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    jwtTokenProvider.init();
  }

  @Test
  void shouldCreateAndParseAccessToken() {
    UUID userId = UUID.randomUUID();
    String email = "user@example.com";
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

    AuthPrincipal authPrincipal = new AuthPrincipal(userId, email, UserRole.USER);
    Authentication inputAuthentication =
        new UsernamePasswordAuthenticationToken(authPrincipal, null, authorities);

    String token = jwtTokenProvider.createToken(TokenType.ACCESS, inputAuthentication);

    Authentication parsedAuthentication = jwtTokenProvider.getAuthentication(token);

    assertThat(parsedAuthentication.getCredentials()).isEqualTo(token);
    assertThat(parsedAuthentication.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_USER");

    AuthPrincipal parsedPrincipal = (AuthPrincipal) parsedAuthentication.getPrincipal();
    assertThat(parsedPrincipal.id()).isEqualTo(userId);
    assertThat(parsedPrincipal.email()).isEqualTo(email);
    assertThat(parsedPrincipal.role()).isEqualTo(UserRole.USER);
  }

  @Test
  void shouldValidateTokenTypeWhenTypesMatch() {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            new AuthPrincipal(UUID.randomUUID(), "u@e.com", UserRole.USER),
            null,
            AuthorityUtils.createAuthorityList("ROLE_USER"));

    String token = jwtTokenProvider.createToken(TokenType.ACCESS, authentication);

    jwtTokenProvider.validateTokenType(token, TokenType.ACCESS);
  }

  @Test
  void shouldThrowJwtExceptionWhenTokenTypeDiffers() {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            new AuthPrincipal(UUID.randomUUID(), "u@e.com", UserRole.USER),
            null,
            AuthorityUtils.createAuthorityList("ROLE_USER"));

    String token = jwtTokenProvider.createToken(TokenType.ACCESS, authentication);

    assertThatThrownBy(() -> jwtTokenProvider.validateTokenType(token, TokenType.REFRESH))
        .isInstanceOf(JwtException.class)
        .hasMessageContaining("Invalid token type");
  }

  @Test
  void shouldSetExpirationAccordingToConfiguredDurations() {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            new AuthPrincipal(UUID.randomUUID(), "u@e.com", UserRole.USER),
            null,
            AuthorityUtils.createAuthorityList("ROLE_USER"));

    String accessToken = jwtTokenProvider.createToken(TokenType.ACCESS, authentication);
    Claims accessClaims = parseWithSameKey(accessToken);
    long accessTtlMillis =
        accessClaims.getExpiration().getTime() - accessClaims.getIssuedAt().getTime();
    assertThat(accessTtlMillis)
        .isBetween(
            Duration.ofMinutes(15).toMillis() - 1000, Duration.ofMinutes(15).toMillis() + 1000);

    String refreshToken = jwtTokenProvider.createToken(TokenType.REFRESH, authentication);
    Claims refreshClaims = parseWithSameKey(refreshToken);
    long refreshTtlMillis =
        refreshClaims.getExpiration().getTime() - refreshClaims.getIssuedAt().getTime();
    assertThat(refreshTtlMillis)
        .isBetween(Duration.ofDays(7).toMillis() - 1000, Duration.ofDays(7).toMillis() + 1000);
  }

  @Test
  void shouldReturnNoAuthoritiesWhenRolesClaimIsMissing() {
    UUID userId = UUID.randomUUID();
    String email = "user@example.com";

    Claims claims =
        Jwts.claims()
            .subject(email)
            .add(JwtClaims.USER_ID_KEY, userId.toString())
            .add(JwtClaims.TOKEN_TYPE_KEY, "access")
            .add(JwtClaims.USER_ROLE_KEY, UserRole.USER.name())
            .build();

    String token =
        Jwts.builder().claims(claims).signWith(buildKey(RAW_SECRET), Jwts.SIG.HS256).compact();

    Authentication authentication = jwtTokenProvider.getAuthentication(token);

    assertThat(authentication.getAuthorities()).isEmpty();

    AuthPrincipal principal = (AuthPrincipal) authentication.getPrincipal();
    assertThat(principal.id()).isEqualTo(userId);
    assertThat(principal.email()).isEqualTo(email);
    assertThat(principal.role()).isEqualTo(UserRole.USER);
  }

  @Test
  void shouldThrowJwtExceptionOnInvalidSignature() {
    UUID userId = UUID.randomUUID();
    String email = "user@example.com";

    Claims claims =
        Jwts.claims()
            .subject(email)
            .add(JwtClaims.USER_ID_KEY, userId.toString())
            .add(JwtClaims.TOKEN_TYPE_KEY, "access")
            .add(JwtClaims.USER_ROLE_KEY, UserRole.USER.name())
            .build();

    SecretKey anotherKey = buildKey(RAW_SECRET + "_different");
    String tokenWithWrongSignature =
        Jwts.builder().claims(claims).signWith(anotherKey, Jwts.SIG.HS256).compact();

    assertThatThrownBy(() -> jwtTokenProvider.getAuthentication(tokenWithWrongSignature))
        .isInstanceOf(JwtException.class);
  }
}
