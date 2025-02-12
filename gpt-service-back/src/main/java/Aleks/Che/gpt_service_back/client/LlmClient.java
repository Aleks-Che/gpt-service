package Aleks.Che.gpt_service_back.client;

import Aleks.Che.gpt_service_back.model.Message;
import Aleks.Che.gpt_service_back.model.MessageType;
import Aleks.Che.gpt_service_back.service.LlmModelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class LlmClient {
    private final RestTemplate restTemplate;
    private final LlmModelService modelService;

    @Value("${llm.server.url}")
    private String llmServerUrl;

    public String summarize(String text) {
        var request = new SummarizeRequest(text, "short");

        var response = restTemplate.postForObject(llmServerUrl + "/summarize", request, String.class);

        return response;
    }

    public void generate(List<Message> messages, Consumer<String> onChunk) {
        var chatMessages = messages.stream()
                .map(msg -> new ChatMessage(
                        msg.getMessageType() == MessageType.REQUEST ? "user" : "assistant",
                        msg.getContent()
                ))
                .collect(Collectors.toList());

        var request = new GenerateRequest(chatMessages);

        restTemplate.execute(
                llmServerUrl + "/generate",
                HttpMethod.POST,
                requestCallback(request),
                responseExtractor(onChunk)
        );
    }


    private RequestCallback requestCallback(GenerateRequest request) {
        return clientHttpRequest -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(clientHttpRequest.getBody(), request);
            clientHttpRequest.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        };
    }

    private ResponseExtractor<Void> responseExtractor(Consumer<String> onChunk) {
        return response -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getBody()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        GenerateResponse resp = mapper.readValue(line, GenerateResponse.class);
                        onChunk.accept(resp.getContent());
                    } catch (RuntimeException e) {
                        // Client disconnected, stop processing
                        break;
                    }
                }
            }
            return null;
        };
    }

    @Data
    @AllArgsConstructor
    private static class SummarizeRequest {
        private String text;
        private String mode;
    }

    @Data
    private static class SummarizeResponse {
        private String intent;
    }

    @Data
    @AllArgsConstructor
    private static class GenerateRequest {
        private List<ChatMessage> messages;
    }

    @Data
    private static class GenerateResponse {
        private String content;
    }

    @Data
    @AllArgsConstructor
    private static class ChatMessage {
        private String role;
        private String content;
    }
}