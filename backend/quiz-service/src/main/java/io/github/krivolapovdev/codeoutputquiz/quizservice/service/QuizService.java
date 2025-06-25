package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.config.cache.CacheNames;
import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.Quiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.exception.QuizNotFoundException;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizRepository;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
  private final ChatClient chatClient;
  private final QuizRepository quizRepository;

  @Cacheable(value = CacheNames.QUIZ_CACHE, key = "#quizRequest")
  public Mono<QuizResponse> getRandomQuiz(QuizRequest quizRequest) {
    log.info("Get random quiz {}", quizRequest);
    return quizRepository
        .findRandomQuiz()
        .map(
            quiz ->
                new QuizResponse(
                    quiz.getId(), quiz.getCode(), quiz.getCorrectAnswer(), quiz.getExplanation()));
  }

  @Cacheable(value = CacheNames.QUIZ_CACHE, key = "#id")
  public Mono<Quiz> getQuizById(UUID id) {
    log.info("Get quiz by id {}", id);
    return quizRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new QuizNotFoundException("Quiz not found with id: " + id)));
  }

  public Flux<String> generateNextQuiz(QuizRequest quizRequest) {
    log.info("Generating next quiz {}", quizRequest);
    String prompt = buildQuizPrompt(quizRequest);
    return chatClient.prompt().user(prompt).stream().content();
  }

  public Mono<List<String>> getSupportedProgrammingLanguages() {
    log.info("Getting supported programming languages");
    return Flux.fromArray(ProgrammingLanguage.values())
        .map(ProgrammingLanguage::getDisplayName)
        .collectList();
  }

  public Mono<List<DifficultyLevel>> getSupportedDifficultyLevels() {
    log.info("Getting supported difficulty levels");
    return Flux.fromArray(DifficultyLevel.values()).collectList();
  }

  private String buildQuizPrompt(QuizRequest request) {
    log.info("Building prompt for {}", request);

    String language = request.programmingLanguage().name().toLowerCase();
    String level = request.difficultyLevel().name().toLowerCase();

    return """
            Generate a code question on the topic "%s" at the %s level.
            Format:
            **Code**:
            ...
            **Options**:
            A) ...
            B) ...
            C) ...
            D) ...
            **Answer**:
            ...
            **Explanation**:
            ...
            """
        .formatted(language, level);
  }
}
