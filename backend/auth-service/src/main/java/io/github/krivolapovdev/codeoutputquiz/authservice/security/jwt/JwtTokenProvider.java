package io.github.krivolapovdev.codeoutputquiz.authservice.security.jwt;

import static java.util.stream.Collectors.joining;

import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.authservice.config.jwt.JwtProperties;
import io.github.krivolapovdev.codeoutputquiz.authservice.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
  private static final String AUTHORITIES_KEY = "roles";
  private static final String USER_ID_KEY = "userId";
  private static final String TOKEN_TYPE_KEY = "tokenType";

  private final JwtProperties jwtProperties;

  private SecretKey secretKey;

  @PostConstruct
  public void init() {
    var secret = Base64.getEncoder().encodeToString(this.jwtProperties.getSecretKey().getBytes());
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public Authentication getAuthentication(String token) {
    Claims claims = parseTokenClaims(token);

    Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

    Collection<? extends GrantedAuthority> authorities =
        authoritiesClaim == null
            ? AuthorityUtils.NO_AUTHORITIES
            : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

    User principal = new User(claims.getSubject(), "", authorities);

    var authenticationToken =
        new UsernamePasswordAuthenticationToken(principal, token, authorities);

    String userIdStr = claims.get(USER_ID_KEY, String.class);
    UUID userId = UUID.fromString(userIdStr);
    AuthDetails authDetails = new AuthDetails(userId);
    authenticationToken.setDetails(authDetails);

    return authenticationToken;
  }

  public String createAccessToken(Authentication auth, UUID userId) {
    return createToken(auth, TokenType.ACCESS, userId);
  }

  public String createRefreshToken(Authentication auth, UUID userId) {
    return createToken(auth, TokenType.REFRESH, userId);
  }

  public void validateTokenType(@NotNull String token, @NotNull TokenType expectedType) {
    Claims claims = parseTokenClaims(token);
    String actual = (String) claims.get(TOKEN_TYPE_KEY);

    if (!expectedType.name().equalsIgnoreCase(actual)) {
      throw new JwtException(
          "Invalid token type: expected " + expectedType + ", but was " + actual);
    }
  }

  private Claims parseTokenClaims(String token) {
    return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
  }

  private String createToken(Authentication authentication, TokenType tokenType, UUID userId) {
    String email = authentication.getName();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    String joinedAuthorities =
        authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(","));

    Claims claims =
        Jwts.claims()
            .subject(email)
            .add(USER_ID_KEY, userId.toString())
            .add(AUTHORITIES_KEY, joinedAuthorities)
            .add(TOKEN_TYPE_KEY, tokenType.name().toLowerCase())
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
