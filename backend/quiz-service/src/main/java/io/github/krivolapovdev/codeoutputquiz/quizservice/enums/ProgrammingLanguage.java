package io.github.krivolapovdev.codeoutputquiz.quizservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgrammingLanguage {
  JAVA("Java"),
  PYTHON("Python"),
  C("C"),
  CPP("C++"),
  GO("Go"),
  CSHARP("C#"),
  JAVASCRIPT("JavaScript"),
  RUST("Rust"),
  POSTGRESQL("PostgreSQL");

  private final String displayName;
}
