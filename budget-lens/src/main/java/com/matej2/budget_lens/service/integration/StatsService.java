package com.matej2.budget_lens.service.integration;

import com.matej2.budget_lens.domain.integration.AvgInflationQueryRequest;
import com.matej2.budget_lens.domain.integration.QueryItem;
import com.matej2.budget_lens.domain.integration.ResponseFormat;
import com.matej2.budget_lens.domain.integration.Selection;
import com.matej2.budget_lens.utils.CsvUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class StatsService {
    private final ObjectMapper objectMapper;
    private final AvgInflationQueryRequest avgInflationQueryRequest;
    @Qualifier("statsRestClient")
    private final RestClient statsRestClient;

    public StatsService(ObjectMapper objectMapper, RestClient statsRestClient) {
        Selection selectionMonths = new Selection("item", CsvUtils.generateMonths());
        Selection selectionIndex = new Selection("item", Arrays.asList("2", "3"));

        ResponseFormat response = new ResponseFormat("json-stat");
        List<QueryItem> query = Arrays.asList(
                new QueryItem("MESEC", selectionMonths),
                new QueryItem("INDEKS", selectionIndex)
        );

        this.avgInflationQueryRequest = new AvgInflationQueryRequest(query, response);
        this.objectMapper = objectMapper;
        this.statsRestClient = statsRestClient;
    }

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

    private Integer parseAvgInflationFromResponse(HashMap<?, ?>  response) {
        HashMap<?, ?> dataset = (HashMap<?, ?>) response.get("dataset");
        List<?> valueListNode = (List<?>) dataset.get("value");

        return (Integer)valueListNode.getLast();
    }

    public Integer getAvgYearlyInflation() throws JsonProcessingException {
        HashMap<String, Object> response = statsRestClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getBody())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return parseAvgInflationFromResponse(Objects.requireNonNull(response));
    }
}
