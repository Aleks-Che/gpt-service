package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.ChatDTO;
import Aleks.Che.gpt_service_back.model.Chat;
import Aleks.Che.gpt_service_back.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    
    @PostMapping
    public ResponseEntity<Chat> createСhat(@RequestBody ChatDTO chatDTO) {
        return ResponseEntity.ok(chatService.createChat(chatDTO));
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
    public ResponseEntity<List<Chat>> getUserChats() {
        return ResponseEntity.ok(chatService.getCurrentUserChats());
    }
}
