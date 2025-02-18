package com.algo.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GptUtil {
    private static final String GPT_API_KEY;
    private static final String GPT_CHAT_API_URL;
    private static Map<String, String> headers;

    private static final String GPT_REQUEST_BODY = """
            {
                "model": "gpt-4o-mini",
                "messages": [
                  {
                    "role": "system",
                    "content": "%s"
                  }
                ]
              }
            """;

    static {
        GPT_API_KEY = "sk-8QoE80jvgRF62CnpJgeuWh5bBkgHp5C7jBYVGaaJNBapDh3C";
        GPT_CHAT_API_URL = "https://api.chatanywhere.tech/v1/chat/completions";
        headers = new HashMap<>();
        headers.put("Authorization", GPT_API_KEY);
        headers.put("Content-Type", "application/json");
    }

    public static String chat(String message) throws IOException {
        String requestBody = GPT_REQUEST_BODY.replace("%s", message);
        String response = HttpRequester.post(GPT_CHAT_API_URL, requestBody, headers);
        JsonNode rootNode = new ObjectMapper().readTree(response);
        return Optional.ofNullable(rootNode)
                .map(node -> node.get("choices"))
                .map(node -> node.get(0))
                .map(node -> node.get("message"))
                .map(node -> node.get("content"))
                .map(JsonNode::asText)
                .orElse("");
    }
}
