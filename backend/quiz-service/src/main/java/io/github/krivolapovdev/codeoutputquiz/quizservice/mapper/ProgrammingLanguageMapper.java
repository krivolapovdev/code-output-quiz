package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import io.github.krivolapovdev.codeoutputquiz.quizservice.enums.ProgrammingLanguage;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.ProgrammingLanguageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgrammingLanguageMapper {
  @Mapping(target = "name", expression = "java(language.name())")
  @Mapping(source = "displayName", target = "displayName")
  ProgrammingLanguageResponse toResponse(ProgrammingLanguage language);
}
