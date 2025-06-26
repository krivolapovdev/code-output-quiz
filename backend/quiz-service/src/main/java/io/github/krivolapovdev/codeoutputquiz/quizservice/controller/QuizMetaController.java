package io.github.krivolapovdev.codeoutputquiz.quizservice.controller;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizMetaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quizzes/meta")
@RequiredArgsConstructor
public class QuizMetaController {
  private final QuizMetaService quizMetaService;

  @GetMapping("/supported-programming-languages")
  public Mono<List<String>> getSupportedProgrammingLanguages() {
    return quizMetaService.getSupportedProgrammingLanguages();
  }

  @GetMapping("/supported-difficulty-levels")
  public Mono<List<DifficultyLevel>> getSupportedDifficultyLevels() {
    return quizMetaService.getSupportedDifficultyLevels();
  }
}
