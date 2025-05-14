package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.chat.ChatDTO;
import Aleks.Che.gpt_service_back.dto.chat.NewChatRequest;
import Aleks.Che.gpt_service_back.model.Chat;
import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.message.Message;
import Aleks.Che.gpt_service_back.model.message.MessageStatus;
import Aleks.Che.gpt_service_back.model.message.MessageType;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.ChatRepository;
import Aleks.Che.gpt_service_back.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Aleks.Che.gpt_service_back.service.MessageService.calculateTokens;

@Service
@Transactional
@AllArgsConstructor
public class ChatServiceImpl implements ChatService{
    
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final LlmModelService modelService;
    private final MessageRepository messageRepository;

    public Chat createChat(String title, NewChatRequest request) {
        User currentUser = userService.getCurrentUser();
        LlmModel model = modelService.getModelById(request.getModelId());
        Chat chat = new Chat();
        chat.setUser(currentUser);
        chat.setTitle(title);
        chat.setModel(model);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        Chat savedChat = chatRepository.save(chat);
        return newMessage(savedChat, request.getMessage());
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

    public Chat newMessage(Chat chat, String content) {
        Message message = new Message();
        message.setChat(chat);
        message.setMessageType(MessageType.USER);
        message.setMessageStatus(MessageStatus.FINISHED);
        message.setContent(content);
        message.setTokensCount(calculateTokens(content));
        message.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        chat.setMessages(new ArrayList<>(List.of(messageRepository.save(message))));
        return chat;
    }
}
