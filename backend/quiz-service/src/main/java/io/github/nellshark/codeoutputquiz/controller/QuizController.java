package io.github.nellshark.codeoutputquiz.controller;

import io.github.nellshark.codeoutputquiz.enums.DifficultyLevel;
import io.github.nellshark.codeoutputquiz.request.QuizRequest;
import io.github.nellshark.codeoutputquiz.service.QuizService;
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
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;

  @GetMapping("/supported-programming-languages")
  public Mono<List<String>> getSupportedProgrammingLanguages() {
    return quizService.getSupportedProgrammingLanguages();
  }

  @GetMapping("/supported-difficulty-levels")
  public Mono<List<DifficultyLevel>> getSupportedDifficultyLevels() {
    return quizService.getSupportedDifficultyLevels();
  }

  @PostMapping(value = "/next", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> generateNextQuiz(@RequestBody QuizRequest quizRequest) {
    return quizService.generateNextQuiz(quizRequest);
  }
}
