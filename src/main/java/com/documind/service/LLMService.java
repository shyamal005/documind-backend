package com.documind.service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@Service
public class LLMService {
private final String API_KEY = System.getenv("GROQ_API_KEY");

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.groq.com/openai/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    // 🔥 DOCUMENT STRUCTURING
    public String processFullDocument(String input) {
String prompt = """
Return ONLY valid JSON.

STRICT RULES:
- All keys MUST be in double quotes
- No invalid fields like: dependent
- No extra objects
- No trailing commas
- No explanations

If unsure → skip that section

Format:

{
  "sections": [
    {
      "title": "string",
      "content": "string"
    }
  ]
}

Document:
""" + input;
        return callLLM(prompt);
    }

    // 🔥 AI CHAT WITH CONTEXT
    public String ask(String question, String document) {

        String prompt = """
        You are an AI assistant.

        Answer ONLY using the document below.
        If answer not found, say "Not found in document".

        DOCUMENT:
        """ + document + """

        QUESTION:
        """ + question;

        return callLLM(prompt);
    }

   private String callLLM(String prompt) {

        String response = webClient.post()
                .uri("/chat/completions")
                .bodyValue("""
                {
                  "model": "llama3-8b-8192",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(prompt.replace("\"", "\\\"")))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            com.fasterxml.jackson.databind.JsonNode node =
                    new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);

            return node
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing LLM response";
        }
    }
}