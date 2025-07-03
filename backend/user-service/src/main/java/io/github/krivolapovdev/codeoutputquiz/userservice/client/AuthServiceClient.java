package io.github.krivolapovdev.codeoutputquiz.userservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {
  private final WebClient webClient;

  @Autowired
  public AuthServiceClient(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://auth-service").build();
  }
}
