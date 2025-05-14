package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.client.LlmClient;
import Aleks.Che.gpt_service_back.dto.chat.ChatDTO;
import Aleks.Che.gpt_service_back.dto.chat.NewChatRequest;
import Aleks.Che.gpt_service_back.model.Chat;
import Aleks.Che.gpt_service_back.service.ChatGenerationService;
import Aleks.Che.gpt_service_back.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final LlmClient llmClient;
    private final ChatGenerationService chatGenerationService;

    @PostMapping("/create")
    public ChatDTO createChat(@RequestBody NewChatRequest request) {
        String title = llmClient.summarize(request.getMessage());
        Chat chat = chatService.createChat(title, request);

        return new ChatDTO(chat);
    }

    @Autowired
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @GetMapping("/{chatId}/generate")
    public SseEmitter generate(
            @PathVariable Long chatId,
            @RequestParam(required = false) Long messageId
    ) {
        log.info("Generate request received for chat: {}, message: {}", chatId, messageId);
        return chatGenerationService.generate(chatId, messageId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chat> updateСhat(@PathVariable Long id,
                                           @RequestBody ChatDTO chatDTO) {
        return ResponseEntity.ok(chatService.updateChat(id, chatDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) throws AccessDeniedException {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<Chat>> getUserChats() {
        return ResponseEntity.ok(chatService.getCurrentUserChats());
    }
}
