package com.matej2.budget_lens.service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.integration.AiCategorizationRequest;
import com.matej2.budget_lens.model.integration.AiCategorizationResponse;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;

@Service
public class GeminiService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final String instructionPrompt = """
        You are a financial advisor. You are analyzing expense transaction summaries from your clients. You need to read transaction lists and categorize them into categories. Input will be formatted as a json with the following structure:
        
        <input>
        
        [
          {
            "expenseId": <number>,
            "description": <text>,
            "merchant": <text>,
            "date": <localDate>
          }
        ]
        
        </input>
        
        Existing categories are listed below. Choose category id from this list:
        <categories>
         %s
        </categories>
        """;
    private final String INSTRUCTIONS;
    private final GoogleGenAiChatOptions.Builder options;

    public GeminiService(ChatModel chatModel, ChatMemory chatMemory, ObjectMapper objectMapper, CategoryRepository categoryRepository, SchemaGenerator schemaGenerator) throws JsonProcessingException {
        this.chatClient = ChatClient.builder(chatModel)
                .build();
        this.objectMapper = objectMapper;

        List<Category> categories = categoryRepository.findAll();
        String categoryListJson = this.objectMapper.writer().writeValueAsString(categories);
        this.INSTRUCTIONS = String.format(this.instructionPrompt, categoryListJson);

        ObjectNode requestJsonSchema = schemaGenerator.generateSchema(AiCategorizationResponse.class);


        ObjectNode arraySchema = JsonNodeFactory.instance.objectNode();
        arraySchema.put("type", "array");
        arraySchema.set("items", requestJsonSchema);

        this.options = GoogleGenAiChatOptions
                .builder()
                .model("gemini-3.5-flash-lite")
                .responseMimeType("application/json")
                .responseSchema(arraySchema.toString());
    }


    public List<AiCategorizationResponse> getAnswer(List<AiCategorizationRequest> requests) throws JsonProcessingException {
        String requestText = objectMapper.writer().writeValueAsString(requests);

        return chatClient
                .prompt(INSTRUCTIONS)
                .user(requestText)
                .options(this.options)
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }
}
