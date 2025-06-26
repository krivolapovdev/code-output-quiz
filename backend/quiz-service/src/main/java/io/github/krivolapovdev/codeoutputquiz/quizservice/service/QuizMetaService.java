package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
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
  public Mono<List<String>> getSupportedProgrammingLanguages() {
    log.info("Getting supported programming languages");
    return Flux.fromArray(ProgrammingLanguage.values())
        .map(ProgrammingLanguage::getDisplayName)
        .collectList();
  }

  public Mono<List<DifficultyLevel>> getSupportedDifficultyLevels() {
    log.info("Getting supported difficulty levels");
    return Flux.fromArray(DifficultyLevel.values()).collectList();
  }
}
