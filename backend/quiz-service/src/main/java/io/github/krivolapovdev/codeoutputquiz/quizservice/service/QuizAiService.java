package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.client.AiClient;
import io.github.krivolapovdev.codeoutputquiz.quizservice.parser.TextQuizParser;
import io.github.krivolapovdev.codeoutputquiz.quizservice.prompt.QuizPromptBuilder;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizAiService {
  private final AiClient aiClient;
  private final QuizPromptBuilder quizPromptBuilder;
  private final TextQuizParser textQuizParser;

  public Mono<QuizView> generateQuiz(@NonNull QuizRequest request) {
    log.info("Generating quiz for: {}", request);

    String prompt = quizPromptBuilder.buildQuizPrompt(request);

    return aiClient
        .sendPrompt(prompt)
        .map(
            text ->
                textQuizParser.parse(
                    text, request.programmingLanguage(), request.difficultyLevel()))
        .doOnError(
            error -> log.warn("Failed to generate quiz for {}: {}", request, error.getMessage()))
        .onErrorResume(error -> Mono.empty());
  }
}
