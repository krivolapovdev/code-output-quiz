package io.github.nellshark.codeoutputquiz.quizservice.service;

import io.github.nellshark.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.nellshark.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.nellshark.codeoutputquiz.quizservice.request.QuizRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
  private final ChatClient chatClient;

  public Flux<String> generateNextQuiz(QuizRequest quizRequest) {
    log.info("Generating next quiz for {}", quizRequest);

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
