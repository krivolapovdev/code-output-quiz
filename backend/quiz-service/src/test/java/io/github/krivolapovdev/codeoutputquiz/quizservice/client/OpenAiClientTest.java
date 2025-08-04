package io.github.krivolapovdev.codeoutputquiz.quizservice.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    List<Generation> generationList = List.of(generation);
    ChatResponse response = new ChatResponse(generationList);
    Flux<ChatResponse> fluxResponse = Flux.just(response);

    ChatClientRequestSpec mockChatClientRequestSpec = mock(ChatClientRequestSpec.class);
    StreamResponseSpec mockStreamResponseSpec = mock(StreamResponseSpec.class);

    when(chatClient.prompt()).thenReturn(mockChatClientRequestSpec);
    when(mockChatClientRequestSpec.user(prompt)).thenReturn(mockChatClientRequestSpec);
    when(mockChatClientRequestSpec.stream()).thenReturn(mockStreamResponseSpec);
    when(mockStreamResponseSpec.chatResponse()).thenReturn(fluxResponse);

    StepVerifier.create(openAiClient.sendPrompt(prompt)).expectNext(aiText).verifyComplete();

    ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
    verify(mockChatClientRequestSpec).user(promptCaptor.capture());
    assertThat(promptCaptor.getValue()).isEqualTo(prompt);
  }

  @Test
  void shouldReturnErrorWhenAiResponseIsEmpty() {
    String prompt = "Empty response expected";

    ChatClientRequestSpec mockRequestSpec = mock(ChatClientRequestSpec.class);
    StreamResponseSpec mockStreamSpec = mock(StreamResponseSpec.class);

    when(chatClient.prompt()).thenReturn(mockRequestSpec);
    when(mockRequestSpec.user(anyString())).thenReturn(mockRequestSpec);
    when(mockRequestSpec.stream()).thenReturn(mockStreamSpec);
    when(mockStreamSpec.chatResponse()).thenReturn(Flux.empty());

    StepVerifier.create(openAiClient.sendPrompt(prompt))
        .expectErrorSatisfies(
            error -> {
              assertThat(error)
                  .isInstanceOf(IllegalStateException.class)
                  .hasMessage("AI response was empty");
            })
        .verify();

    ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
    verify(mockRequestSpec).user(promptCaptor.capture());
    assertThat(promptCaptor.getValue()).isEqualTo(prompt);
  }
}
