package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.ChatDTO;
import Aleks.Che.gpt_service_back.model.ChatEntity;
import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.ChatRepository;
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
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final LlmModelService modelService;
    
    public ChatEntity createChat(ChatDTO dto) {
        User currentUser = userService.getCurrentUser();
        LlmModel model = modelService.getModelById(dto.getModelId());

        ChatEntity chat = new ChatEntity();
        chat.setUser(currentUser);
        chat.setTitle(dto.getTitle());
        chat.setModel(model);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        
        return chatRepository.save(chat);
    }
    
    public ChatEntity updateChat(Long id, ChatDTO dto) {
        ChatEntity chat = chatRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Беседа не найдена"));
            
        chat.setTitle(dto.getTitle());
        chat.setUpdatedAt(LocalDateTime.now());
        
        return chatRepository.save(chat);
    }
    
    public List<ChatEntity> getCurrentUserChats() {
        User currentUser = userService.getCurrentUser();
        return chatRepository.findByUserOrderByUpdatedAtDesc(currentUser);
    }

    public ChatEntity getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Беседа с ID " + id + " не найдена"));
    }

    public void deleteChat(Long id) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser();
        ChatEntity chat = chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Беседа не найдена"));

        if (!chat.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Нет прав на удаление этой беседы");
        }

        chatRepository.deleteById(id);
    }

}
