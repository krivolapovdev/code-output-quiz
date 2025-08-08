package io.github.krivolapovdev.codeoutputquiz.quizservice.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.client.ChatClient.StreamResponseSpec;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class OpenAiClientTest {
  @Mock private ChatClient chatClient;
  @InjectMocks private OpenAiClient openAiClient;

  @Test
  void shouldReturnResponseFromAi() {
    String prompt = "What is Java?";
    String aiText = "Java is a programming language.";
    AssistantMessage assistantMessage = new AssistantMessage(aiText);
    Generation generation = new Generation(assistantMessage);
    ChatResponse response = new ChatResponse(List.of(generation));
    Flux<ChatResponse> fluxResponse = Flux.just(response);

    ChatClientRequestSpec mockChatClientRequestSpec = mock(ChatClientRequestSpec.class);
    StreamResponseSpec mockStreamResponseSpec = mock(StreamResponseSpec.class);

    when(chatClient.prompt()).thenReturn(mockChatClientRequestSpec);
    when(mockChatClientRequestSpec.user(prompt)).thenReturn(mockChatClientRequestSpec);
    when(mockChatClientRequestSpec.stream()).thenReturn(mockStreamResponseSpec);
    when(mockStreamResponseSpec.chatResponse()).thenReturn(fluxResponse);

    openAiClient.sendPrompt(prompt).as(StepVerifier::create).expectNext(aiText).verifyComplete();

    verify(mockChatClientRequestSpec).user(prompt);
  }

  @Test
  void shouldReturnErrorWhenAiResponseIsEmpty() {
    String prompt = "Empty response expected";

    ChatClientRequestSpec mockRequestSpec = mock(ChatClientRequestSpec.class);
    StreamResponseSpec mockStreamSpec = mock(StreamResponseSpec.class);

    when(chatClient.prompt()).thenReturn(mockRequestSpec);
    when(mockRequestSpec.user(prompt)).thenReturn(mockRequestSpec);
    when(mockRequestSpec.stream()).thenReturn(mockStreamSpec);
    when(mockStreamSpec.chatResponse()).thenReturn(Flux.empty());

    openAiClient
        .sendPrompt(prompt)
        .as(StepVerifier::create)
        .expectErrorSatisfies(
            error ->
                assertThat(error)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("AI response was empty"))
        .verify();

    verify(mockRequestSpec).user(prompt);
  }
}
