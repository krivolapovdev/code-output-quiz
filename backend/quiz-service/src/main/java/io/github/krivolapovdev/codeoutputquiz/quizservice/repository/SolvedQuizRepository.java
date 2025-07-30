package io.github.krivolapovdev.codeoutputquiz.quizservice.repository;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.SolvedQuiz;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedQuizRepository extends ReactiveCrudRepository<SolvedQuiz, Void> {}
