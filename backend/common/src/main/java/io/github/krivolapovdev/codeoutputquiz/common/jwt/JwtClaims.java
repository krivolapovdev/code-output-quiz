package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtClaims {
  public static final String AUTHORITIES_KEY = "roles";
  public static final String USER_ID_KEY = "id";
  public static final String TOKEN_TYPE_KEY = "tokenType";
  public static final String USER_ROLE_KEY = "userRole";
}
