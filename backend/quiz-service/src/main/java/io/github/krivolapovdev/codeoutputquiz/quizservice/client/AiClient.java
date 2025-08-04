package io.github.krivolapovdev.codeoutputquiz.quizservice.client;

import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

public interface AiClient {
  Mono<String> sendPrompt(@NonNull String prompt);
}
