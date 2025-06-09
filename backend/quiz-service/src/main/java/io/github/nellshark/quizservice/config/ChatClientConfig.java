package io.github.nellshark.quizservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
  @Bean
  public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
    return ChatClient.create(openAiChatModel);
  }
}
