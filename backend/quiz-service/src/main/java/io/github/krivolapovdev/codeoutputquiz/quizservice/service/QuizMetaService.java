package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.ProgrammingLanguageMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.ProgrammingLanguageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizMetaService {
  private final ProgrammingLanguageMapper programmingLanguageMapper;

  public Mono<List<ProgrammingLanguageResponse>> getSupportedProgrammingLanguages() {
    log.info("Getting supported programming languages");
    return Flux.fromArray(ProgrammingLanguage.values())
        .map(programmingLanguageMapper::toResponse)
        .collectList();
  }

  public Mono<List<DifficultyLevel>> getSupportedDifficultyLevels() {
    log.info("Getting supported difficulty levels");
    return Flux.fromArray(DifficultyLevel.values()).collectList();
  }
}
