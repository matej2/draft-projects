package com.matej2.budget_lens.service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.*;
import com.matej2.budget_lens.domain.integration.AiCategorizationRequest;
import com.matej2.budget_lens.domain.integration.AiCategorizationResponse;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseFormatTextJsonSchemaConfig;
import com.openai.models.responses.ResponseTextConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatGPTService {
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private SchemaGenerator getGenerator() {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
                SchemaVersion.DRAFT_2019_09,
                OptionPreset.PLAIN_JSON
        );

        configBuilder.with(Option.FORBIDDEN_ADDITIONAL_PROPERTIES_BY_DEFAULT);

        SchemaGeneratorConfig config = configBuilder.build();
        return new SchemaGenerator(config);
    }

    private String generateSchema(Class<?> clazz) throws JsonProcessingException {
        SchemaGenerator generator = getGenerator();

        JsonNode schemaNode = generator.generateSchema(clazz);
        return objectMapper.writeValueAsString(schemaNode);
    }

    public void getResponse(List<AiCategorizationRequest> aiCategorizationRequestList) throws JsonProcessingException {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

        String aiCategorizationRequest = objectMapper.writeValueAsString(aiCategorizationRequestList);
        String outputSchema = generateSchema(AiCategorizationResponse.class);

        ResponseCreateParams params =
                ResponseCreateParams
                        .builder()
                        .instructions("""
                                You are a financial advisor. You are analyzing expense transaction summaries from your clients. You need to read transaction lists and categorize them into categories. You also analyze expense necessity from the received transaction lists. Input will be formatted as a json with the following structure:
                                
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
                                """)
                        .input(aiCategorizationRequest)
                        .model("gpt-6-astra")
                        .text(
                            ResponseTextConfig.builder()
                                .format(
                                    ResponseFormatTextJsonSchemaConfig.builder()
                                        .name("expenseList")
                                        .strict(true)
                                        .schema(
                                                Objects.requireNonNull(JsonValue.from(outputSchema)
                                                        .convert(ResponseFormatTextJsonSchemaConfig.Schema.class)))
                                        .build())
                                .build())
                        .build();

        Response response = client.responses().create(params);
        response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .forEach(outputText -> System.out.println(outputText.text()));
    }
}
