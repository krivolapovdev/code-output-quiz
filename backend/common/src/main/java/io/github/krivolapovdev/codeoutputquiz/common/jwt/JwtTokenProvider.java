package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import static java.util.stream.Collectors.joining;

import io.github.krivolapovdev.codeoutputquiz.common.enums.TokenType;
import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
  private static final String AUTHORITIES_KEY = "roles";
  private static final String USER_ID_KEY = "id";
  private static final String TOKEN_TYPE_KEY = "tokenType";
  private static final String USER_ROLE_KEY = "userRole";

  private final JwtProperties jwtProperties;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    var secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public @NonNull Authentication getAuthentication(@NonNull String token) {
    Claims claims = parseTokenClaims(token);

    Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
    Collection<? extends GrantedAuthority> authorities =
        authoritiesClaim == null
            ? AuthorityUtils.NO_AUTHORITIES
            : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

    String userIdStr = claims.get(USER_ID_KEY, String.class);
    UUID userId = UUID.fromString(userIdStr);

    String userRoleStr = claims.get(USER_ROLE_KEY, String.class);
    UserRole userRole = UserRole.valueOf(userRoleStr);

    var authUser = new AuthPrincipal(userId, claims.getSubject(), userRole);

    return new UsernamePasswordAuthenticationToken(authUser, token, authorities);
  }

  public void validateTokenType(@NonNull String token, @NonNull TokenType expectedType) {
    Claims claims = parseTokenClaims(token);
    String actual = (String) claims.get(TOKEN_TYPE_KEY);

    if (!expectedType.name().equalsIgnoreCase(actual)) {
      throw new JwtException(
          "Invalid token type: expected " + expectedType + ", but was " + actual);
    }
  }

  private @NonNull Claims parseTokenClaims(@NonNull String token) {
    return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
  }

  public @NonNull String createToken(
      @NonNull TokenType tokenType, @NonNull Authentication authentication) {
    AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    String joinedAuthorities =
        authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(","));

    Claims claims =
        Jwts.claims()
            .subject(authPrincipal.email())
            .add(USER_ID_KEY, authPrincipal.id())
            .add(AUTHORITIES_KEY, joinedAuthorities)
            .add(TOKEN_TYPE_KEY, tokenType.name().toLowerCase())
            .add(USER_ROLE_KEY, authPrincipal.role())
            .build();

    Date now = new Date();

    Duration ttl =
        tokenType == TokenType.ACCESS
            ? jwtProperties.getAccessTokenExpiration()
            : jwtProperties.getRefreshTokenExpiration();

    Date expiration = new Date(now.getTime() + ttl.toMillis());

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(this.secretKey, Jwts.SIG.HS256)
        .compact();
  }
}
