package com.matej2.budget_lens.domain.integration;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatRequest {
    private String model;
    private String service_tier;
    private List<Message> input;
    private double temperature;
    private Map<String, Object> text;
    private Map<String, String> reasoning;
    private List<Object> tools;
    private boolean store;
    private List<String> include;

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.service_tier = "default";

        Map<String, String> textFormat = Map.of("type", "text");
        this.text = Map.of(
                "format", textFormat,
                "verbosity", "medium"
        );

        this.reasoning = Map.of(
                "effort", "medium",
                "mode", "standard",
                "summary", "auto"
        );

        this.tools = List.of();
        this.store = true;
        this.include = List.of(
                "reasoning.encrypted_content",
                "web_search_call.action.sources"
        );
    }
}