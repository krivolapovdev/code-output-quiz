package ru.codeoutputquiz.aiservice.controller;

import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
  @Value("${eureka.instance.instance-id}")
  private String id;

  @GetMapping
  public Mono<String> hello() {
    return Mono.just(id);
  }

  @GetMapping("/test")
  public Flux<String> test() {
    return Flux.fromIterable(List.of("Product A", "Product B", "Product C"))
        .delayElements(Duration.ofSeconds(1));
  }
}
