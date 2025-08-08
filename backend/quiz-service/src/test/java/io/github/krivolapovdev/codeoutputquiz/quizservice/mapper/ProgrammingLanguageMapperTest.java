package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.ProgrammingLanguageResponse;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProgrammingLanguageMapperTest {
  private final ProgrammingLanguageMapper programmingLanguageMapper =
      Mappers.getMapper(ProgrammingLanguageMapper.class);

  @Test
  void shouldMapEnumToResponse() {
    ProgrammingLanguage language = ProgrammingLanguage.JAVA;

    ProgrammingLanguageResponse response = programmingLanguageMapper.toResponse(language);

    assertThat(response.name()).isEqualTo("JAVA");
    assertThat(response.displayName()).isEqualTo("Java");
  }

  @Test
  void shouldMapAllEnumValuesCorrectly() {
    Stream.of(ProgrammingLanguage.values())
        .map(programmingLanguageMapper::toResponse)
        .forEach(
            response -> {
              ProgrammingLanguage language = ProgrammingLanguage.valueOf(response.name());
              assertThat(response.displayName()).isEqualTo(language.getDisplayName());
            });
  }
}
