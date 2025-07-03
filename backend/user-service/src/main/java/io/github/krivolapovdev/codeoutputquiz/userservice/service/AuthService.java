package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.userservice.client.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
  private final AuthServiceClient authServiceClient;
}
