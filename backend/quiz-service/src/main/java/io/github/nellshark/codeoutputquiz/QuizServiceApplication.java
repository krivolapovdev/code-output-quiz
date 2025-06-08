package io.github.nellshark.codeoutputquiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QuizServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(QuizServiceApplication.class, args);
  }
}
