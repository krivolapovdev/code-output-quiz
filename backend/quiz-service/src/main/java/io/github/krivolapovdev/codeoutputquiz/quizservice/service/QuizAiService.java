package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.prompt.QuizPromptBuilder;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizAiService {
  private final ChatClient chatClient;
  private final QuizPromptBuilder quizPromptBuilder;

  public Flux<String> generateQuiz(QuizRequest quizRequest) {
    log.info("Generating quiz {}", quizRequest);
    String prompt = quizPromptBuilder.buildQuizPrompt(quizRequest);
    return chatClient.prompt().user(prompt).stream().content();
  }
}
