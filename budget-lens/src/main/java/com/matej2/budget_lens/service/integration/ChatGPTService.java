package com.matej2.budget_lens.service.integration;

import org.springframework.stereotype.Service;

@Service
public class ChatGPTService {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public ChatResponse processTransactionSummary() {
        ChatRequest request = new ChatRequest();
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

        return response.getChoices().get(0).getMessage().getContent();
    }
}
