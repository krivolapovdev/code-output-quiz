package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.krivolapovdev.codeoutputquiz.authservice.entity.User;
import io.github.krivolapovdev.codeoutputquiz.authservice.request.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserFactoryTest {
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserFactory userFactory;

  @Test
  void shouldCreateUserWithEncodedPassword() {
    var email = "user@example.com";
    var rawPassword = "secret";
    var encodedPassword = "encoded-secret";
    var request = new AuthRequest(email, rawPassword);

    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

    User user = userFactory.create(request);

    assertThat(user.getEmail()).isEqualTo(email);
    assertThat(user.getPassword()).isEqualTo(encodedPassword);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(passwordEncoder).encode(captor.capture());
    assertThat(captor.getValue()).isEqualTo(rawPassword);
  }
}
