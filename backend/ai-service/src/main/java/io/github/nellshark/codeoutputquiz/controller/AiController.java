package io.github.nellshark.codeoutputquiz.controller;

import io.github.nellshark.codeoutputquiz.enums.DifficultyLevel;
import io.github.nellshark.codeoutputquiz.request.QuizRequest;
import io.github.nellshark.codeoutputquiz.service.AiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
  private final AiService aiService;

  @GetMapping("/supported-languages")
  public Mono<List<String>> getSupportedProgrammingLanguages() {
    return aiService.getSupportedProgrammingLanguages();
  }

  @GetMapping("/supported-difficulty-levels")
  public Mono<List<DifficultyLevel>> getSupportedDifficultyLevels() {
    return aiService.getSupportedDifficultyLevels();
  }

  @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> generateNextQuiz(@RequestBody QuizRequest quizRequest) {
    return aiService.generateNextQuiz(quizRequest);
  }
}
