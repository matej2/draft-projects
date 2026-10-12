package com.matej2.budget_lens.domain.integration;

@Data
public class ChatRequest {

    private String model;
    private String service_tier;

    private List<Message> input;
    private double temperature;

    private Map<String, Object> text;

    private Map<String, String> reasoning;
    private boolean store;
    private List<String> include;

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.service_tier = "default";

        Map<String, Sring> textFormat = Map.of("type", "text");
        this.text = Map.of("format", textFormat);

        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}