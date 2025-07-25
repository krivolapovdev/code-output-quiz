package io.github.nellshark.codeoutputquiz.notificationservice.config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
  @Bean
  public MailjetClient mailjetClient(MailjetEmailProperties mailjetEmailProperties) {
    ClientOptions options =
        ClientOptions.builder()
            .apiKey(mailjetEmailProperties.getPublicKey())
            .apiSecretKey(mailjetEmailProperties.getPrivateKey())
            .build();
    return new MailjetClient(options);
  }
}
