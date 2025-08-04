package io.github.krivolapovdev.codeoutputquiz.quizservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
class OpenAiClient implements AiClient {
  private final ChatClient chatClient;

  @Override
  public @NonNull Mono<String> sendPrompt(@NonNull String prompt) {
    log.info("Sending prompt to AI: {}", prompt);

    return chatClient.prompt().user(prompt).stream()
        .chatResponse()
        .map(ChatResponse::getResult)
        .map(result -> result.getOutput().getText())
        .reduce(new StringBuilder(), StringBuilder::append)
        .map(StringBuilder::toString)
        .filter(s -> !s.isEmpty())
        .doOnSuccess(text -> log.info("Successfully received AI response: {}", text))
        .switchIfEmpty(Mono.error(new IllegalStateException("AI response was empty")))
        .doOnError(error -> log.warn("Failed to get AI response: {}", error.getMessage(), error));
  }
}
