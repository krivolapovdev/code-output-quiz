package io.github.krivolapovdev.codeoutputquiz.quizservice.config.jwt;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
  private String secretKey;
  private Duration accessTokenExpiration;
  private Duration refreshTokenExpiration;
}
