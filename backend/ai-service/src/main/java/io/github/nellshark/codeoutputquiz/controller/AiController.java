package io.github.nellshark.codeoutputquiz.controller;

import io.github.nellshark.codeoutputquiz.request.QuizRequest;
import io.github.nellshark.codeoutputquiz.response.QuizResponse;
import io.github.nellshark.codeoutputquiz.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
  private final AiService aiService;

  @PostMapping("/chat")
  public Flux<QuizResponse> generateNextQuiz(@RequestBody QuizRequest quizRequest) {
    return aiService.generateNextQuiz(quizRequest);
  }
}
