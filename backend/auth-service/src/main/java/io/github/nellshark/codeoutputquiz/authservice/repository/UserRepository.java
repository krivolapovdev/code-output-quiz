package io.github.nellshark.codeoutputquiz.authservice.repository;

import io.github.nellshark.codeoutputquiz.authservice.entity.User;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
  @Query(
      """
      SELECT
          *
      FROM
          users
      WHERE
          email = :email
      """)
  Mono<User> findByEmail(String email);
}
