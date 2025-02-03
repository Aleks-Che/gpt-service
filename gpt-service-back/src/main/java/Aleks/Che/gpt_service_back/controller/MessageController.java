package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.MessageDTO;
import Aleks.Che.gpt_service_back.model.Message;
import Aleks.Che.gpt_service_back.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {
    
    private final MessageService messageService;
    
    @PostMapping("/conversation/{conversationId}")
    public ResponseEntity<Message> sendMessage(@PathVariable Long conversationId,
                                               @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.sendMessage(conversationId, messageDTO));
    }
    
    @PostMapping("/conversation/{conversationId}/with-file")
    public ResponseEntity<Message> sendMessageWithFile(@PathVariable Long conversationId,
                                                     @RequestPart("message") MessageDTO messageDTO,
                                                     @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(messageService.sendMessageWithFile(conversationId, messageDTO, file));
    }
    
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long messageId,
                                               @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.updateMessage(messageId, messageDTO));
    }
    
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/conversation/{conversationId}/after/{messageId}")
    public ResponseEntity<Void> deleteMessagesAfter(@PathVariable Long conversationId,
                                                  @PathVariable Long messageId) {
        messageService.deleteMessagesAfter(conversationId, messageId);
        return ResponseEntity.noContent().build();
    }
}
