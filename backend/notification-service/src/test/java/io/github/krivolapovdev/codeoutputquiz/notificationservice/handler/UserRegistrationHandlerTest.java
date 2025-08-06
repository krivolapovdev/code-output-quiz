package io.github.krivolapovdev.codeoutputquiz.notificationservice.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.kafka.TopicNames;
import io.github.krivolapovdev.codeoutputquiz.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserRegistrationHandlerTest {
  @Mock private EmailService emailService;
  @InjectMocks private UserRegistrationHandler userRegistrationHandler;

  @Test
  void shouldExposeCorrectTopic() {
    assertThat(userRegistrationHandler.topic()).isEqualTo(TopicNames.USER_REGISTRATION);
  }

  @Test
  void shouldSendWelcomeEmail() {
    String email = "test@example.com";

    when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(Mono.empty());

    userRegistrationHandler.handleEvent(email).as(StepVerifier::create).verifyComplete();

    ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

    verify(emailService).sendEmail(eq(email), subjectCaptor.capture(), contentCaptor.capture());

    String subject = subjectCaptor.getValue();
    String content = contentCaptor.getValue();

    assertThat(subject).contains("Welcome to CodeOutputQuiz!");
    assertThat(content)
        .contains(email)
        .contains("https://github.com/krivolapovdev/code-output-quiz")
        .contains("CodeOutputQuiz");
  }
}
