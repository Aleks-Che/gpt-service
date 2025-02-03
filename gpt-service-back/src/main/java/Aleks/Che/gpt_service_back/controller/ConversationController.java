package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.ConversationDTO;
import Aleks.Che.gpt_service_back.model.Conversation;
import Aleks.Che.gpt_service_back.service.ConversationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@AllArgsConstructor
public class ConversationController {
    
    private final ConversationService conversationService;
    
    @PostMapping
    public ResponseEntity<Conversation> createConversation(@RequestBody ConversationDTO conversationDTO) {
        return ResponseEntity.ok(conversationService.createConversation(conversationDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Conversation> updateConversation(@PathVariable Long id, 
                                                         @RequestBody ConversationDTO conversationDTO) {
        return ResponseEntity.ok(conversationService.updateConversation(id, conversationDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long id) throws AccessDeniedException {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Conversation>> getUserConversations() {
        return ResponseEntity.ok(conversationService.getCurrentUserConversations());
    }
}
