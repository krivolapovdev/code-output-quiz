package io.github.nellshark.codeoutputquiz.quizservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgrammingLanguage {
  PYTHON("Python"),
  JAVA("Java"),
  C("C"),
  CPP("C++"),
  GO("Go"),
  CSHARP("C#"),
  JAVASCRIPT("JavaScript"),
  RUST("Rust"),
  SWIFT("Swift"),
  PHP("PHP");

  private final String displayName;
}
