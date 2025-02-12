package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.chat.ChatDTO;
import Aleks.Che.gpt_service_back.dto.chat.NewChatRequest;
import Aleks.Che.gpt_service_back.model.Chat;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface ChatService {
    Chat createChat(String title, NewChatRequest request);

    Chat updateChat(Long id, ChatDTO dto);

    List<Chat> getCurrentUserChats();

    Chat getChatById(Long id);

    void deleteChat(Long id) throws AccessDeniedException;

    Chat newMessage(Chat chat, String content);
}