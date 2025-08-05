package io.github.krivolapovdev.codeoutputquiz.common.jwt;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
  private String secretKey;
  private Duration accessTokenExpiration;
  private Duration refreshTokenExpiration;
}
