package io.github.krivolapovdev.codeoutputquiz.quizservice.mapper;

import io.github.krivolapovdev.codeoutputquiz.quizservice.response.QuizResponse;
import io.github.krivolapovdev.codeoutputquiz.quizservice.view.QuizView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizMapper {
  QuizResponse toResponse(QuizView quizView);
}
