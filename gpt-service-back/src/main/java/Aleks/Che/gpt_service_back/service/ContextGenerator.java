package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.model.Chat;
import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.message.Message;
import Aleks.Che.gpt_service_back.model.message.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContextGenerator {
    private static final int ROLE_PREFIX_TOKENS = 2; // Примерная оценка токенов для "user: " или "bot: "
    
    public String generateContext(Chat chat) {
        LlmModel model = chat.getModel();
        List<Message> messages = chat.getMessages();
        
        int totalTokens = calculateTotalTokens(messages);
        
        if (totalTokens <= model.getMaxTokens()) {
            return formatMessages(messages);
        }
        
        // Пробуем использовать сжатые версии сообщений
        List<Message> optimizedMessages = new ArrayList<>();
        Message firstMessage = messages.get(0);
        List<Message> lastMessages = messages.subList(Math.max(1, messages.size() - 5), messages.size());
        
        optimizedMessages.add(firstMessage);
        optimizedMessages.addAll(lastMessages);
        
        int optimizedTokens = calculateOptimizedTokens(optimizedMessages);
        
        if (optimizedTokens <= model.getMaxTokens()) {
            return formatOptimizedMessages(optimizedMessages);
        }
        
        // Если все еще не помещаемся, оставляем только первое и последнее сообщение
        return formatOptimizedMessages(Arrays.asList(firstMessage, messages.get(messages.size() - 1)));
    }
    
    private int calculateTotalTokens(List<Message> messages) {
        return messages.stream()
            .mapToInt(msg -> msg.getTokensCount() + ROLE_PREFIX_TOKENS)
            .sum();
    }
    
    private int calculateOptimizedTokens(List<Message> messages) {
        return messages.stream()
            .mapToInt(msg -> {
                if (msg.getSummarizeTokensCount() != null) {
                    return msg.getSummarizeTokensCount() + ROLE_PREFIX_TOKENS;
                }
                return msg.getTokensCount() + ROLE_PREFIX_TOKENS;
            })
            .sum();
    }
    
    private String formatMessages(List<Message> messages) {
        StringBuilder context = new StringBuilder();
        for (Message msg : messages) {
            String role = msg.getMessageType() == MessageType.USER ? "user: " : "bot: ";
            context.append(role).append(msg.getContent()).append("\n");
        }
        return context.toString();
    }
    
    private String formatOptimizedMessages(List<Message> messages) {
        StringBuilder context = new StringBuilder();
        for (Message msg : messages) {
            String role = msg.getMessageType() == MessageType.USER ? "user: " : "bot: ";
            String content = msg.getContentSummarize() != null ? 
                msg.getContentSummarize() : msg.getContent();
            context.append(role).append(content).append("\n");
        }
        return context.toString();
    }
}
