package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.client.LlmClient;
import Aleks.Che.gpt_service_back.model.Chat;
import Aleks.Che.gpt_service_back.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGenerationService {
    private final ChatService chatService;
    private final MessageService messageService;
    private final LlmClient llmClient;
    private final ExecutorService executorService;

    public SseEmitter generate(Long chatId, Long messageId) {
        SseEmitter emitter = new SseEmitter();
        Chat chat = chatService.getChatById(chatId);

        if (messageId != null) {
            messageService.deleteMessagesAfter(chatId, messageId);
            chat = chatService.getChatById(chatId);
        }

        Chat finalChat = chat;
        executorService.submit(() -> generateResponse(finalChat, emitter));

        return emitter;
    }

private void generateResponse(Chat chat, SseEmitter emitter) {
    AtomicBoolean isCompleted = new AtomicBoolean(false);
    StringBuilder fullResponse = new StringBuilder();
    
    emitter.onCompletion(() -> isCompleted.set(true));
    emitter.onTimeout(() -> isCompleted.set(true));
    
    try {
        llmClient.generate(
                chat.getMessages(),
                response -> {
                    if (isCompleted.get()) {
                        throw new RuntimeException("Client disconnected");
                    }
                    try {
                        fullResponse.append(response);
                        emitter.send(response);
                    } catch (IOException e) {
                        isCompleted.set(true);
                        emitter.completeWithError(e);
                    }
                }
        );
        
        if (!isCompleted.get()) {
            messageService.saveMessage(
                chat.getId(),
                fullResponse.toString(),
                MessageType.RESPONSE
            );
            emitter.complete();
        }
    } catch (Exception e) {
        if (!isCompleted.get()) {
            emitter.completeWithError(e);
        }
    }
}

}

