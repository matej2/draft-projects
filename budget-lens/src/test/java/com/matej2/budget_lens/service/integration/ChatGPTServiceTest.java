package com.matej2.budget_lens.service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matej2.budget_lens.domain.integration.AiCategorizationRequest;
import com.matej2.budget_lens.domain.integration.AiCategorizationResponse;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "spring.ai.openai.api-key=6423567"
})
public class ChatGPTServiceTest {

    @Mock
    OpenAIOkHttpClient.Builder clientBuilder;

    ChatGPTService underTest;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
       ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        this.objectMapper = objectMapper;
        this.underTest = new ChatGPTService(objectMapper);
    }

    private static List<AiCategorizationRequest> generateTestRequestData() {
        return List.of(
                new AiCategorizationRequest(
                        31,
                        "Test",
                        "Test merchant",
                        LocalDate.now()
                ),
                new AiCategorizationRequest(
                        53,
                        "Test",
                        "Test merchant",
                        LocalDate.now()
                ),
                new AiCategorizationRequest(
                        94,
                        "Test",
                        "Test merchant",
                        LocalDate.now()
                )
        );
    }

    private static List<AiCategorizationResponse> generateTestResponseData() {
        return List.of(
                new AiCategorizationResponse(
                        31,
                        1
                ),
                new AiCategorizationResponse(
                        53,
                        3
                ),
                new AiCategorizationResponse(
                        94,
                        2
                )
        );
    }

    @Test
    void testThatGetGeneratorReturnsValidValue() throws JsonProcessingException {
        List<AiCategorizationRequest> testData = generateTestRequestData();

        OpenAIOkHttpClient.Builder clientBuilder = mock(OpenAIOkHttpClient.Builder.class, Answers.RETURNS_DEEP_STUBS);
        OpenAIClient client = mock(OpenAIClient.class, Answers.RETURNS_DEEP_STUBS);
        when(clientBuilder.apiKey(anyString()).build()).thenReturn(client);

        Response mockResponse = mock(Response.class, Answers.RETURNS_DEEP_STUBS);
        when(client.responses().create(any(ResponseCreateParams.class))).thenReturn(mockResponse);

        ResponseOutputText mockOutputText = mock(ResponseOutputText.class);
        when(mockOutputText.text()).thenReturn(this.objectMapper.writeValueAsString(generateTestResponseData()));

        ResponseOutputMessage.Content mockContent = mock(ResponseOutputMessage.Content.class);
        when(mockContent.outputText()).thenReturn(Optional.of(mockOutputText));

        ResponseOutputMessage mockMessage = mock(ResponseOutputMessage.class);
        when(mockMessage.content()).thenReturn(List.of(mockContent));

        ResponseOutputItem mockOutputItem = mock(ResponseOutputItem.class);
        when(mockResponse.output().stream()).thenReturn(Stream.of(mockOutputItem));

        try (MockedStatic<OpenAIOkHttpClient> utilities = Mockito.mockStatic(OpenAIOkHttpClient.class)) {
            utilities.when(OpenAIOkHttpClient::builder).thenReturn(clientBuilder);

            underTest.getResponse(testData);
        }
    }
}
