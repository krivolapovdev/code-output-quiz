package io.github.krivolapovdev.codeoutputquiz.quizservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChatClientConfig {
  @Bean
  public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
    return ChatClient.create(openAiChatModel);
  }
}
