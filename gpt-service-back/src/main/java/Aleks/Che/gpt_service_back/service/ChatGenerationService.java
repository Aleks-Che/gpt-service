package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.client.LlmClient;
import Aleks.Che.gpt_service_back.model.Chat;
import Aleks.Che.gpt_service_back.model.message.MessageStatus;
import Aleks.Che.gpt_service_back.model.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
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
        AtomicBoolean isMessageSaved = new AtomicBoolean(false);
        StringBuilder fullResponse = new StringBuilder();
        Object lock = new Object();

        emitter.onCompletion(() -> {
            synchronized (lock) {
                if (!isCompleted.get() && !isMessageSaved.get()) {
                    log.debug("Saving from onCompletion");
                    saveIncompleteResponse(chat.getId(), fullResponse.toString());
                    isMessageSaved.set(true);
                }
                isCompleted.set(true);
            }
        });

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
                            synchronized (lock) {
                                if (e.getCause() instanceof ClientAbortException && !isMessageSaved.get()) {
                                    log.debug("Saving from ClientAbortException handler");
                                    saveIncompleteResponse(chat.getId(), fullResponse.toString());
                                    isMessageSaved.set(true);
                                }
                                isCompleted.set(true);
                                emitter.completeWithError(e);
                            }
                        }
                    }
            );

            if (!isCompleted.get()) {
                synchronized (lock) {
                    if (!isMessageSaved.get()) {
                        messageService.saveMessage(
                                chat.getId(),
                                fullResponse.toString(),
                                MessageType.ASSISTANT,
                                MessageStatus.FINISHED
                        );
                        isMessageSaved.set(true);
                    }
                    isCompleted.set(true);
                    emitter.complete();
                }
            }
        } catch (Exception e) {
            synchronized (lock) {
                if (!isCompleted.get() && !isMessageSaved.get()) {
                    saveIncompleteResponse(chat.getId(), fullResponse.toString());
                    isMessageSaved.set(true);
                }
                emitter.completeWithError(e);
            }
        }
    }


    private void saveIncompleteResponse(Long chatId, String content) {
        try {
            if (content != null && !content.isEmpty()) {
                messageService.saveMessage(
                        chatId,
                        content,
                        MessageType.ASSISTANT,
                        MessageStatus.INCOMPLETE
                );
            }
        } catch (Exception e) {
            log.error("Failed to save incomplete response", e);
        }
    }
}

