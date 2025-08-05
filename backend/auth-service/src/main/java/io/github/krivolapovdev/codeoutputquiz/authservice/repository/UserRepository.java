package io.github.krivolapovdev.codeoutputquiz.authservice.repository;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
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
  Mono<User> findByEmail(@NonNull @Param("email") String email);
}
