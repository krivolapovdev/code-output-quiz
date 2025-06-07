package io.github.nellshark.codeoutputquiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgrammingLanguage {
  JAVA("Java"),
  PYTHON("Python"),
  GO("Go"),
  RUBY("Ruby"),
  CSHARP("C#"),
  KOTLIN("Kotlin"),
  SCALA("Scala"),
  JAVASCRIPT("JavaScript"),
  RUST("Rust"),
  SWIFT("Swift"),
  TYPESCRIPT("TypeScript"),
  PHP("PHP"),
  DART("Dart"),
  HASKELL("Haskell"),
  PERL("Perl"),
  LUA("Lua"),
  ELIXIR("Elixir"),
  C("C"),
  CPP("C++");

  private final String displayName;
}
