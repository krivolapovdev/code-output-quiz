package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.AnswerChoicesJsonMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.parser.TextQuizParser;
import io.github.krivolapovdev.codeoutputquiz.quizservice.prompt.QuizPromptBuilder;
import io.github.krivolapovdev.codeoutputquiz.quizservice.repository.QuizViewRepository;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizAiService {
  private final QuizViewRepository quizViewRepository;
  private final AnswerChoicesJsonMapper answerChoicesJsonMapper;
  private final ChatClient chatClient;
  private final QuizPromptBuilder quizPromptBuilder;
  private final TextQuizParser textQuizParser;

  @PostConstruct
  public void init() {
    Flux.interval(Duration.ofMinutes(1), Duration.ofMinutes(10))
        .onBackpressureDrop()
        .concatMap(tick -> generateNewQuizzes())
        .subscribe();
  }

  private Flux<Void> generateNewQuizzes() {
    return Flux.fromStream(
            Stream.of(ProgrammingLanguage.values())
                .flatMap(
                    language ->
                        Stream.of(DifficultyLevel.values())
                            .map(level -> new QuizRequest(language, level))))
        .doOnNext(request -> log.info("Generating quiz for: {}", request))
        .concatMap(this::generateQuiz)
        .flatMap(this::saveQuizWithChoices)
        .thenMany(Flux.empty());
  }

  private Mono<QuizView> generateQuiz(QuizRequest request) {
    return Mono.fromCallable(
            () -> {
              String prompt = quizPromptBuilder.buildQuizPrompt(request);
              ChatResponse chatResponse = chatClient.prompt().user(prompt).call().chatResponse();

              if (chatResponse == null) {
                throw new IllegalStateException("AI returned null for request: " + request);
              }

              return textQuizParser.parse(
                  chatResponse.getResult().getOutput().getText(),
                  request.programmingLanguage(),
                  request.difficultyLevel());
            })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorResume(
            error -> {
              log.warn("Failed to generate quiz for {}: {}", request, error.getMessage());
              return Mono.empty();
            });
  }

  private Mono<QuizView> saveQuizWithChoices(@NonNull QuizView quizView) {
    return quizViewRepository
        .insertQuizWithChoices(
            quizView.getCode(),
            quizView.getProgrammingLanguage().name(),
            quizView.getDifficultyLevel().name(),
            quizView.getCorrectAnswer().name(),
            quizView.getExplanation(),
            answerChoicesJsonMapper.toJson(quizView.getAnswerChoices()))
        .doOnSuccess(ignored -> log.info("Successfully saved quiz: {}", quizView.getCode()))
        .doOnError(error -> log.error("Failed to save quiz: {}", quizView.getCode(), error))
        .thenReturn(quizView);
  }
}
