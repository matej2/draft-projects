package com.matej2.budget_lens.service.integration;

import com.matej2.budget_lens.domain.integration.ChatRequest;
import com.matej2.budget_lens.domain.integration.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGPTService {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestClient restClient;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public ChatResponse processTransactionSummary() {
        ChatRequest request = new ChatRequest();
        ChatResponse response = restClient.postForObject(apiUrl, request, ChatResponse.class);

        return response.getChoices().get(0).getMessage().getContent();
    }
}
