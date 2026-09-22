package com.example.expense_tracker.service.integration;

import com.example.expense_tracker.config.singleton.AvgInflationQuerySingleton;
import com.example.expense_tracker.domain.AvgInflationQueryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final ObjectMapper objectMapper;
    private AvgInflationQueryRequest avgInflationQueryRequest = AvgInflationQuerySingleton.getInstance();
    private final RestClient statsRestClient;

    private String getBody() throws JsonProcessingException {
        return objectMapper.writeValueAsString(avgInflationQueryRequest);
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (
                    BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }
        else {
            throw new IOException("Request returned error code");
        }
        connection.disconnect();
        return String.valueOf(response);
    }

    private Double parseAvgInflationFromResponse(HashMap<String, HashMap<String, List<Double>>>  response) {
        Map<String, List<Double>> dataset = response.get("dataset");
        List<Double> valueListNode = dataset.get("value");

        return valueListNode.getLast();
    }

    public Double getAvgYearlyInflation() {
        HashMap<String, HashMap<String, List<Double>>> response = statsRestClient
                .post()
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return parseAvgInflationFromResponse(Objects.requireNonNull(response));
    }
}
