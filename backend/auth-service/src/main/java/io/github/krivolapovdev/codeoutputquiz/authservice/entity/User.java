package io.github.krivolapovdev.codeoutputquiz.authservice.entity;

import io.github.krivolapovdev.codeoutputquiz.authservice.enums.UserRoles;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
public class User {
  @Id private UUID id;

  @Column("email")
  private String email;

  @Column("password")
  private String password;

  @Column("role")
  private UserRoles role;

  @CreatedDate
  @Column("created_at")
  private OffsetDateTime createdAt;

  @LastModifiedDate
  @Column("updated_at")
  private OffsetDateTime updatedAt;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
