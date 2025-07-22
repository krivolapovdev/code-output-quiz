package io.github.nellshark.codeoutputquiz.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mailjet")
@Data
public class MailjetEmailProperties {
  private String publicKey;
  private String privateKey;
  private String senderEmail;
}
