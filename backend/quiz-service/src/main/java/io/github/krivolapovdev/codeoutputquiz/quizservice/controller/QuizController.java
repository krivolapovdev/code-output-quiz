package io.github.krivolapovdev.codeoutputquiz.quizservice.controller;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;

  @GetMapping("/{id}")
  public Mono<QuizResponse> getQuizById(@PathVariable UUID id) {
    return quizService.getQuizById(id);
  }

  @GetMapping("/random")
  public Mono<QuizResponse> getRandomQuiz(
      @RequestParam(defaultValue = "JAVA") ProgrammingLanguage programmingLanguage,
      @RequestParam(defaultValue = "BEGINNER") DifficultyLevel difficultyLevel) {
    return quizService.getRandomQuiz(programmingLanguage, difficultyLevel);
  }

  @GetMapping("/unsolved")
  @PreAuthorize("isAuthenticated()")
  public Mono<QuizResponse> getUserUnsolvedQuiz(
      @RequestParam(defaultValue = "JAVA") ProgrammingLanguage programmingLanguage,
      @RequestParam(defaultValue = "BEGINNER") DifficultyLevel difficultyLevel,
      Authentication authentication) {
    return quizService.getUserUnsolvedQuiz(programmingLanguage, difficultyLevel, authentication);
  }
}
