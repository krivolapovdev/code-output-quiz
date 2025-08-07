package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.common.jwt.AuthDetails;
import io.github.krivolapovdev.codeoutputquiz.userservice.entity.UserQuizReaction;
import io.github.krivolapovdev.codeoutputquiz.userservice.repository.UserQuizReactionRepository;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserQuizReactionRequest;
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
class UserQuizReactionServiceTest {
  @Mock private UserQuizReactionRepository userQuizReactionRepository;
  @InjectMocks private UserQuizReactionService userQuizReactionService;

  @Test
  void shouldSaveQuizReactionSuccessfully() {
    UUID userId = UUID.randomUUID();
    UUID quizId = UUID.randomUUID();
    boolean liked = true;

    UserQuizReactionRequest request = new UserQuizReactionRequest(quizId, liked);

    AuthDetails authDetails = mock(AuthDetails.class);
    when(authDetails.userId()).thenReturn(userId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getDetails()).thenReturn(authDetails);

    UserQuizReaction expectedReaction = new UserQuizReaction(userId, quizId, liked);

    when(userQuizReactionRepository.saveUserQuizReaction(expectedReaction))
        .thenReturn(Mono.empty());

    userQuizReactionService
        .reactToQuiz(request, authentication)
        .as(StepVerifier::create)
        .verifyComplete();

    verify(userQuizReactionRepository).saveUserQuizReaction(expectedReaction);
  }
}
