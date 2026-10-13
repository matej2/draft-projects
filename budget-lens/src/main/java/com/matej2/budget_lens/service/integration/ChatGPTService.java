package com.matej2.budget_lens.service.integration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public void getResponse() {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

        ResponseCreateParams params =
                ResponseCreateParams
                        .builder()
                        .input("Say this is a test")
                        .model("gpt-6-astra")
                        .build();

        Response response = client.responses().create(params);
        response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .forEach(outputText -> System.out.println(outputText.text()));
    }
}
