package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.ChatDTO;
import Aleks.Che.gpt_service_back.model.Chat;
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
    
    public Chat createChat(ChatDTO dto) {
        User currentUser = userService.getCurrentUser();
        LlmModel model = modelService.getModelById(dto.getModelId());

        Chat chat = new Chat();
        chat.setUser(currentUser);
        chat.setTitle(dto.getTitle());
        chat.setModel(model);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        
        return chatRepository.save(chat);
    }
    
    public Chat updateChat(Long id, ChatDTO dto) {
        Chat chat = chatRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Беседа не найдена"));
            
        chat.setTitle(dto.getTitle());
        chat.setUpdatedAt(LocalDateTime.now());
        
        return chatRepository.save(chat);
    }
    
    public List<Chat> getCurrentUserChats() {
        User currentUser = userService.getCurrentUser();
        return chatRepository.findByUserOrderByUpdatedAtDesc(currentUser);
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Беседа с ID " + id + " не найдена"));
    }

    public void deleteChat(Long id) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser();
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Беседа не найдена"));

        if (!chat.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Нет прав на удаление этой беседы");
        }

        chatRepository.deleteById(id);
    }

}
