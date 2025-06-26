package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import io.github.krivolapovdev.codeoutputquiz.quizservice.entity.Quiz;
import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizMapper {
  QuizResponse toResponse(Quiz quiz);
}
