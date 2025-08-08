package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.quizservice.client.AiClient;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.parser.TextQuizParser;
import io.github.krivolapovdev.codeoutputquiz.quizservice.prompt.QuizPromptBuilder;
import io.github.krivolapovdev.codeoutputquiz.quizservice.request.QuizRequest;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class QuizAiServiceTest {
  @Mock private AiClient aiClient;
  @Mock private QuizPromptBuilder quizPromptBuilder;
  @Mock private TextQuizParser textQuizParser;
  @InjectMocks private QuizAiService quizAiService;

  private static final QuizRequest REQ =
      new QuizRequest(ProgrammingLanguage.JAVA, DifficultyLevel.BEGINNER);

  @Test
  void shouldGenerateQuizFromAiText() {
    String prompt = "prompt-for-java-beginner";
    String aiText = "AI: quiz text...";
    QuizView parsed = mock(QuizView.class);

    when(quizPromptBuilder.buildQuizPrompt(REQ)).thenReturn(prompt);
    when(aiClient.sendPrompt(prompt)).thenReturn(Mono.just(aiText));
    when(textQuizParser.parse(aiText, REQ.programmingLanguage(), REQ.difficultyLevel()))
        .thenReturn(parsed);

    quizAiService
        .generateQuiz(REQ)
        .as(StepVerifier::create)
        .assertNext(q -> assertThat(q).isSameAs(parsed))
        .verifyComplete();

    verify(quizPromptBuilder).buildQuizPrompt(REQ);
    verify(aiClient).sendPrompt(prompt);
    verify(textQuizParser).parse(aiText, REQ.programmingLanguage(), REQ.difficultyLevel());
    verifyNoMoreInteractions(quizPromptBuilder, aiClient, textQuizParser);
  }

  @Test
  void shouldPropagateErrorWhenAiClientFails() {
    String prompt = "prompt-for-java-beginner";
    RuntimeException boom = new RuntimeException("boom");

    when(quizPromptBuilder.buildQuizPrompt(REQ)).thenReturn(prompt);
    when(aiClient.sendPrompt(prompt)).thenReturn(Mono.error(boom));

    quizAiService
        .generateQuiz(REQ)
        .as(StepVerifier::create)
        .expectErrorSatisfies(ex -> assertThat(ex).isSameAs(boom))
        .verify();

    verify(quizPromptBuilder).buildQuizPrompt(REQ);
    verify(aiClient).sendPrompt(prompt);
    verifyNoInteractions(textQuizParser);
    verifyNoMoreInteractions(quizPromptBuilder, aiClient);
  }

  @Test
  void shouldPropagateErrorWhenParserFails() {
    String prompt = "prompt-for-java-beginner";
    String aiText = "AI: quiz text...";
    RuntimeException parseFail = new RuntimeException("parse failed");

    when(quizPromptBuilder.buildQuizPrompt(REQ)).thenReturn(prompt);
    when(aiClient.sendPrompt(prompt)).thenReturn(Mono.just(aiText));
    when(textQuizParser.parse(aiText, REQ.programmingLanguage(), REQ.difficultyLevel()))
        .thenThrow(parseFail);

    quizAiService
        .generateQuiz(REQ)
        .as(StepVerifier::create)
        .expectErrorSatisfies(ex -> assertThat(ex).isSameAs(parseFail))
        .verify();

    verify(quizPromptBuilder).buildQuizPrompt(REQ);
    verify(aiClient).sendPrompt(prompt);
    verify(textQuizParser).parse(aiText, REQ.programmingLanguage(), REQ.difficultyLevel());
    verifyNoMoreInteractions(quizPromptBuilder, aiClient, textQuizParser);
  }
}
