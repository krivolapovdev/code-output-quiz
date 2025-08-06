package io.github.krivolapovdev.codeoutputquiz.authservice.factory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

@ExtendWith(MockitoExtension.class)
class CookieFactoryTest {
  @InjectMocks private CookieFactory cookieFactory;

  @Test
  void shouldCreateSecureHttpOnlyCookie() {
    String name = "test_cookie";
    String value = "some_value";
    String path = "/";
    Duration maxAge = Duration.ofMinutes(30);

    ResponseCookie cookie = cookieFactory.create(name, value, path, maxAge);

    assertThat(cookie.getName()).isEqualTo(name);
    assertThat(cookie.getValue()).isEqualTo(value);
    assertThat(cookie.getPath()).isEqualTo(path);
    assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
    assertThat(cookie.isHttpOnly()).isTrue();
    assertThat(cookie.isSecure()).isTrue();
    assertThat(cookie.getSameSite()).isEqualTo("Strict");
  }
}
