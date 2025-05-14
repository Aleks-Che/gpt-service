package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.MessageDTO;
import Aleks.Che.gpt_service_back.model.message.Message;
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
    
//    @PostMapping("/chat/{chatId}")
//    public ResponseEntity<Message> sendMessage(@PathVariable Long chatId,
//                                               @RequestBody MessageDTO messageDTO) {
//        return ResponseEntity.ok(messageService.saveMessage(chatId, messageDTO));
//    }
    
    @PostMapping("/chat/{chatId}/with-file")
    public ResponseEntity<Message> sendMessageWithFile(@PathVariable Long chatId,
                                                     @RequestPart("message") MessageDTO messageDTO,
                                                     @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(messageService.sendMessageWithFile(chatId, messageDTO, file));
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
    
    @DeleteMapping("/chat/{chatId}/after/{messageId}")
    public ResponseEntity<Void> deleteMessagesAfter(@PathVariable Long chatId,
                                                  @PathVariable Long messageId) {
        messageService.deleteMessagesAfter(chatId, messageId);
        return ResponseEntity.noContent().build();
    }
}
