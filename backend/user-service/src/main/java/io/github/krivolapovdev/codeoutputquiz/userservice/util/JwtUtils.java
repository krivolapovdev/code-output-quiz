package io.github.krivolapovdev.codeoutputquiz.userservice.util;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
  public static final String HEADER_PREFIX = "Bearer ";

  public static Optional<String> extractAccessToken(String header) {
    if (StringUtils.hasText(header) && header.startsWith(HEADER_PREFIX)) {
      return Optional.of(header.substring(HEADER_PREFIX.length()).trim());
    }
    return Optional.empty();
  }
}
