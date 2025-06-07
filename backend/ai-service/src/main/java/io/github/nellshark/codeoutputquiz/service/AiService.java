package io.github.nellshark.codeoutputquiz.service;

import io.github.nellshark.codeoutputquiz.request.QuizRequest;
import io.github.nellshark.codeoutputquiz.response.QuizResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
  private final ChatClient chatClient;

  public Flux<QuizResponse> generateNextQuiz(QuizRequest quizRequest) {
    log.info("Generating next quiz for {}", quizRequest);
    String programmingLanguageName = quizRequest.programmingLanguage().name();
    Flux<String> content =
        chatClient.prompt().user("tell me about" + programmingLanguageName).stream().content();
    return content.map(QuizResponse::new);
  }
}
