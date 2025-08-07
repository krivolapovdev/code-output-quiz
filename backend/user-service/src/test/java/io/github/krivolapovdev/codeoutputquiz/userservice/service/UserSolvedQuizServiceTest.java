package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.enums.AnswerChoice;
import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthPrincipal;
import io.github.krivolapovdev.codeoutputquiz.common.kafka.event.QuizSolvedEvent;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserSolvedQuiz;
import io.github.krivolapovdev.codeoutputquiz.userservice.notifier.QuizSolvedNotifier;
import io.github.krivolapovdev.codeoutputquiz.userservice.repository.UserSolvedQuizRepository;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserSolvedQuizRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserSolvedQuizServiceTest {
  @Mock private UserSolvedQuizRepository userSolvedQuizRepository;
  @Mock private QuizSolvedNotifier quizSolvedNotifier;
  @InjectMocks private UserSolvedQuizService userSolvedQuizService;

  @Test
  void shouldAddUserSolvedQuizAndSendQuizSolvedEvent() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    AnswerChoice selectedAnswer = AnswerChoice.B;

    UserSolvedQuizRequest request = new UserSolvedQuizRequest(quizId, selectedAnswer);
    UserSolvedQuiz expectedEntity = new UserSolvedQuiz(quizId, userId, selectedAnswer);
    QuizSolvedEvent expectedEvent = new QuizSolvedEvent(userId, quizId);

    AuthPrincipal authPrincipal = new AuthPrincipal(userId, "user@example.com", UserRole.USER);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(authPrincipal);

    when(userSolvedQuizRepository.addUserSolvedQuiz(expectedEntity)).thenReturn(Mono.empty());
    when(quizSolvedNotifier.sendQuizSolvedEvent(expectedEvent)).thenReturn(Mono.empty());

    userSolvedQuizService
        .addUserSolvedQuiz(request, authentication)
        .as(StepVerifier::create)
        .verifyComplete();

    verify(userSolvedQuizRepository).addUserSolvedQuiz(expectedEntity);
    verify(quizSolvedNotifier).sendQuizSolvedEvent(expectedEvent);
    verify(authentication).getPrincipal();
  }
}
