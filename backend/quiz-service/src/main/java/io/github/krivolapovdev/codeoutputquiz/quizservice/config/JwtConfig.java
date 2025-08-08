package io.github.krivolapovdev.codeoutputquiz.quizservice.config;

import io.github.krivolapovdev.codeoutputquiz.common.jwt.JwtProperties;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.JwtTokenProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
  @Bean
  public JwtTokenProvider jwtTokenProvider(JwtProperties jwtProperties) {
    return new JwtTokenProvider(jwtProperties);
  }
}
