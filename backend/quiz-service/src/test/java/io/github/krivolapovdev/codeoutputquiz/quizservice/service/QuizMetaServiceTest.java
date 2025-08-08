package io.github.krivolapovdev.codeoutputquiz.quizservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.DifficultyLevel;
import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.mapper.ProgrammingLanguageMapper;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.ProgrammingLanguageResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class QuizMetaServiceTest {
  @Mock private ProgrammingLanguageMapper programmingLanguageMapper;
  @InjectMocks private QuizMetaService quizMetaService;

  @Test
  void shouldReturnAllProgrammingLanguagesMappedInEnumOrder() {
    Map<ProgrammingLanguage, ProgrammingLanguageResponse> mapping =
        Stream.of(ProgrammingLanguage.values())
            .collect(
                Collectors.toMap(
                    lang -> lang,
                    lang -> new ProgrammingLanguageResponse(lang.name(), lang.getDisplayName())));

    mapping.forEach(
        (lang, resp) -> when(programmingLanguageMapper.toResponse(lang)).thenReturn(resp));

    List<ProgrammingLanguageResponse> expectedInOrder =
        Stream.of(ProgrammingLanguage.values()).map(mapping::get).toList();

    quizMetaService
        .getSupportedProgrammingLanguages()
        .as(StepVerifier::create)
        .assertNext(
            actual -> {
              assertThat(actual).hasSize(expectedInOrder.size());
              assertThat(actual).containsExactlyElementsOf(expectedInOrder);
            })
        .verifyComplete();
  }

  @Test
  void shouldPropagateErrorIfMapperFailsForAnyLanguage() {
    ProgrammingLanguage first = ProgrammingLanguage.values()[0];

    when(programmingLanguageMapper.toResponse(first))
        .thenThrow(new RuntimeException("mapping failed"));

    quizMetaService
        .getSupportedProgrammingLanguages()
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            ex ->
                assertThat(ex)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("mapping failed"))
        .verify();

    verify(programmingLanguageMapper, only()).toResponse(first);
  }

  @Test
  void shouldReturnAllDifficultyLevelsInEnumOrder() {
    List<DifficultyLevel> expected = List.of(DifficultyLevel.values());

    quizMetaService
        .getSupportedDifficultyLevels()
        .as(StepVerifier::create)
        .assertNext(
            actual -> {
              assertThat(actual).hasSize(expected.size());
              assertThat(actual).containsExactlyElementsOf(expected);
            })
        .verifyComplete();
  }
}
