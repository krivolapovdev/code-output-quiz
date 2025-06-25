package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.Quiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuizMapper {
  QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

  QuizResponse toResponse(Quiz quiz);
}
