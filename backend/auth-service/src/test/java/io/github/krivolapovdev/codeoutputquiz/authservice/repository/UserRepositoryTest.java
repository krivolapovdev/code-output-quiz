package io.github.krivolapovdev.codeoutputquiz.authservice.repository;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(TestcontainersConfig.class)
class UserRepositoryTest {
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll().block();
  }

  @Test
  void shouldFindUserByEmail() {
    String email = "test@example.com";
    User user = new User(email, "hashedPassword");
    userRepository.save(user).block();

    userRepository
        .findByEmail(email)
        .as(StepVerifier::create)
        .expectNextMatches(u -> u.getEmail().equals(email))
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyWhenEmailNotFound() {
    User user = new User("test@example.com", "hashedPassword");
    userRepository.save(user).block();

    userRepository.findByEmail("notfound@example.com").as(StepVerifier::create).verifyComplete();
  }
}
