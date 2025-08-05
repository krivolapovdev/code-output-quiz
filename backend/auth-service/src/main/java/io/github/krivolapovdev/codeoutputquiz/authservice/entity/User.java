package io.github.krivolapovdev.codeoutputquiz.authservice.entity;

import io.github.krivolapovdev.codeoutputquiz.common.enums.UserRole;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table("users")
@Data
public class User {
  @Id private UUID id;

  @Column("email")
  private String email;

  @Column("password")
  private String password;

  @Column("role")
  private UserRole role;

  @Column("created_at")
  private OffsetDateTime createdAt;

  @Column("updated_at")
  private OffsetDateTime updatedAt;

  public User(@NonNull String email, @NonNull String password) {
    this.email = email;
    this.password = password;
  }

  public @NonNull String getEmail() {
    return email.toLowerCase();
  }
}
