package io.github.nellshark.codeoutputquiz.authservice.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
public class User {
  @Id
  private UUID id;

  @Column("email")
  private String email;

  @Column("password")
  private String password;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
