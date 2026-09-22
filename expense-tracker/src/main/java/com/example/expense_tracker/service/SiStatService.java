package com.example.expense_tracker.service;

import com.example.expense_tracker.config.singleton.AvgInflationQuerySingleton;
import com.example.expense_tracker.domain.AvgInflationQueryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class SiStatService {
    private URL url;
    private final ObjectMapper objectMapper;
    @Qualifier("siStatUrl")
    private final URL siStatUrl;
    private AvgInflationQueryRequest avgInflationQueryRequest = AvgInflationQuerySingleton.getInstance();

    private String getBody() throws JsonProcessingException {
        return objectMapper.writeValueAsString(avgInflationQueryRequest);
    }

    private void setOutputStream(HttpURLConnection connection) throws IOException {
        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(getBody());
        osw.flush();
        osw.close();
        os.close();
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

    private Double parseAvgInflationFromResponse(String response) throws JsonProcessingException {
        JsonNode root = new ObjectMapper().readTree(response);

        JsonNode valueListNode = root.at("/dataset/value");

        Double calculatedAvgInf = valueListNode.get(valueListNode.size()-1).asDouble();
        return calculatedAvgInf;
    }


    public Double getAvgYearlyInflation() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        setOutputStream(conn);
        conn.connect();

        String response = getResponse(conn);
        return parseAvgInflationFromResponse(response);
    }
}
