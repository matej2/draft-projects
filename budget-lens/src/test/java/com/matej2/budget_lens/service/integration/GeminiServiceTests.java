package com.matej2.budget_lens.service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.integration.AiCategorizationRequest;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeminiServiceTests {
    @Mock
    ChatClient chatClient;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    ObjectWriter objectWriter;
    GoogleGenAiChatOptions.Builder options;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    SchemaGenerator schemaGenerator;
    GeminiService underTest;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    ArgumentCaptor<String> instructionTextCaptor =
            ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> promptTextCaptor =
            ArgumentCaptor.forClass(String.class);

    @Test
    void testGetAnswer() throws JsonProcessingException {
        List<AiCategorizationRequest> categorizationRequests = List.of(
                new AiCategorizationRequest(3, "test", "merchant1", LocalDate.now()),
                new AiCategorizationRequest(4, "test", "merchant2", LocalDate.now()),
                new AiCategorizationRequest(5, "test", "merchant3", LocalDate.now())
        );
        String categoriesJson = """
        [
            {
                "id": 3,
                "name": "category1"
            },
            {
                "id": 4,
                "name": "category2"
            },
            {
                "id": 5,
                "name": "category3"
            }
        ]
        """;
        String aiCategorizationJson = """
        [
            {
                "expenseId": 3,
                "description":  "test",
                "merchant": "merchant1",
                "date": "2026-11-08"
            },
            {
                "expenseId": 4,
                "description":  "test",
                "merchant": "merchant2",
                "date": "2026-11-08"
            }
        ]
        """;

        when(objectMapper.writer()).thenReturn(objectWriter);
        when(objectWriter.writeValueAsString(argThat(
                list -> list != null && ((List<?>)list).getFirst() instanceof Category
        ))).thenReturn(categoriesJson);
        when(objectWriter.writeValueAsString(argThat(
                list -> list != null && ((List<?>)list).getFirst() instanceof AiCategorizationRequest
        ))).thenReturn(aiCategorizationJson);

        when(categoryRepository.findAll()).thenReturn(List.of(new Category()));


        when(chatClient.prompt(anyString()))
                .thenReturn(requestSpec);

        when(requestSpec.user(anyString()))
                .thenReturn(requestSpec);

        when(requestSpec.options(any(GoogleGenAiChatOptions.Builder.class)))
                .thenReturn(requestSpec);

        when(requestSpec.call())
                .thenReturn(responseSpec);

        underTest = new GeminiService(
                chatClient,
                objectMapper,
                categoryRepository,
                schemaGenerator
        );
        underTest.getAnswer(categorizationRequests);

        verify(chatClient).prompt(instructionTextCaptor.capture());
        verify(requestSpec).user(promptTextCaptor.capture());
        verify(requestSpec).call();

        assertThat(instructionTextCaptor.getValue()).contains("""
        [
            {
                "id": 3,
                "name": "category1"
            },
            {
                "id": 4,
                "name": "category2"
            },
            {
                "id": 5,
                "name": "category3"
            }
        ]
        """);
        assertThat(promptTextCaptor.getValue()).contains("""
        [
            {
                "expenseId": 3,
                "description":  "test",
                "merchant": "merchant1",
                "date": "2026-11-08"
            },
            {
                "expenseId": 4,
                "description":  "test",
                "merchant": "merchant2",
                "date": "2026-11-08"
            }
        ]
        """);
    }
}
