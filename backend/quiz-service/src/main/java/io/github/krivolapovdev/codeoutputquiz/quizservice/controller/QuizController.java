package io.github.krivolapovdev.codeoutputquiz.quizservice.controller;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.service.QuizService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;

  @GetMapping
  public Mono<QuizResponse> getRandomQuiz(
      @RequestParam ProgrammingLanguage programmingLanguage,
      @RequestParam DifficultyLevel difficultyLevel) {
    QuizRequest request = new QuizRequest(programmingLanguage, difficultyLevel);
    return quizService.getRandomQuiz(request);
  }

  @GetMapping("/{id}")
  public Mono<QuizResponse> getQuizById(@PathVariable UUID id) {
    return quizService.getQuizById(id);
  }

  @PostMapping(value = "/next", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> generateQuiz(@RequestBody QuizRequest quizRequest) {
    return quizService.generateQuiz(quizRequest);
  }
}
