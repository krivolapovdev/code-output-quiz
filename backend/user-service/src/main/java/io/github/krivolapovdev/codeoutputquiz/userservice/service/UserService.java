package io.github.krivolapovdev.codeoutputquiz.userservice.service;

import io.github.krivolapovdev.codeoutputquiz.userservice.repository.UserSolvedQuizRepository;
import io.github.krivolapovdev.codeoutputquiz.userservice.request.UserSolvedQuizRequest;
import io.github.krivolapovdev.codeoutputquiz.userservice.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserSolvedQuizRepository userSolvedQuizRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public Flux<String> getUserSolvedQuizzes() {
    return ReactiveSecurityContextHolder.getContext()
        .map(ctx -> (String) ctx.getAuthentication().getCredentials())
        .map(jwtTokenProvider::extractUserIdFromToken)
        .doOnNext(userId -> log.info("Fetching solved quizzes for userId: {}", userId))
        .flatMapMany(userSolvedQuizRepository::findAllSolvedQuizzesByUserId)
        .map(sq -> sq.getQuizId().toString());
  }

  public Mono<Void> addUserSolvedQuiz(UserSolvedQuizRequest request) {
    return ReactiveSecurityContextHolder.getContext()
        .map(ctx -> (String) ctx.getAuthentication().getCredentials())
        .map(jwtTokenProvider::extractUserIdFromToken)
        .doOnNext(
            userId ->
                log.info("Adding solved quiz for userId: {}, quizId: {}", userId, request.quizId()))
        .flatMap(
            userId ->
                userSolvedQuizRepository.addUserSolvedQuiz(
                    userId, request.quizId(), request.answerChoice().name()));
  }
}
