package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.ConversationDTO;
import Aleks.Che.gpt_service_back.model.Conversation;
import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.ConversationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ConversationService {
    
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final LlmModelService modelService;
    
    public Conversation createConversation(ConversationDTO dto) {
        User currentUser = userService.getCurrentUser();
        LlmModel model = modelService.getModelById(dto.getModelId());
        
        Conversation conversation = new Conversation();
        conversation.setUser(currentUser);
        conversation.setTitle(dto.getTitle());
        conversation.setModel(model);
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setUpdatedAt(LocalDateTime.now());
        
        return conversationRepository.save(conversation);
    }
    
    public Conversation updateConversation(Long id, ConversationDTO dto) {
        Conversation conversation = conversationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Беседа не найдена"));
            
        conversation.setTitle(dto.getTitle());
        conversation.setUpdatedAt(LocalDateTime.now());
        
        return conversationRepository.save(conversation);
    }
    
    public List<Conversation> getCurrentUserConversations() {
        User currentUser = userService.getCurrentUser();
        return conversationRepository.findByUserOrderByUpdatedAtDesc(currentUser);
    }

    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Беседа с ID " + id + " не найдена"));
    }

    public void deleteConversation(Long id) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser();
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Беседа не найдена"));

        if (!conversation.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Нет прав на удаление этой беседы");
        }

        conversationRepository.deleteById(id);
    }

}
