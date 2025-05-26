package ru.codeoutputquiz.aiservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
  @GetMapping
  public String hello() {
    return "Hello World";
  }
}
