package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.MessageDTO;
import Aleks.Che.gpt_service_back.model.ChatEntity;
import Aleks.Che.gpt_service_back.model.Message;
import Aleks.Che.gpt_service_back.model.MessageType;
import Aleks.Che.gpt_service_back.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final FileStorageService fileStorageService;
    
    public Message sendMessage(Long chatId, MessageDTO dto) {
        ChatEntity chat = chatService.getChatById(chatId);
        
        Message message = new Message();
        message.setChat(chat);
        message.setMessageType(MessageType.REQUEST);
        message.setContent(dto.getContent());
        message.setTokensCount(calculateTokens(dto.getContent()));
        message.setCreatedAt(LocalDateTime.now());
        
        Message savedMessage = messageRepository.save(message);
        
        // Здесь логика отправки запроса к LLM и сохранение ответа
        Message response = processLlmResponse(chat, dto.getContent());
        
        return savedMessage;
    }
    
    public Message sendMessageWithFile(Long chatId, MessageDTO dto, MultipartFile file) {
        String filePath = fileStorageService.storeFile(file);
        
        Message message = new Message();
        message.setFilePath(filePath);
        message.setFileSizeBytes(file.getSize());
        message.setFileMimeType(file.getContentType());
        // Заполнение остальных полей как в sendMessage
        
        return messageRepository.save(message);
    }
    
    public void deleteMessagesAfter(Long chatId, Long messageId) {
        messageRepository.deleteByChatIdAndIdGreaterThan(chatId, messageId);
    }
    
    private Integer calculateTokens(String content) {
        // Логика подсчета токенов
        return content.length() / 4; // Упрощенный пример
    }
    
    private Message processLlmResponse(ChatEntity chat, String prompt) {
        // Логика обработки запроса к LLM модели и сохранение ответа
        return null; // Заглушка
    }

    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Сообщение с ID " + messageId + " не найдено"));

        // Если есть прикрепленный файл - удаляем его
        if (message.getFilePath() != null) {
            fileStorageService.deleteFile(message.getFilePath());
        }

        messageRepository.deleteById(messageId);
        log.info("Удалено сообщение с ID: {}", messageId);
    }

    public Message updateMessage(Long messageId, MessageDTO dto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Сообщение с ID " + messageId + " не найдено"));

        message.setContent(dto.getContent());
        message.setTokensCount(calculateTokens(dto.getContent()));

        // Если пришел новый файл
        if (dto.getFile() != null) {
            // Удаляем старый файл если был
            if (message.getFilePath() != null) {
                fileStorageService.deleteFile(message.getFilePath());
            }
            // Сохраняем новый файл
            String filePath = fileStorageService.storeFile(dto.getFile());
            message.setFilePath(filePath);
            message.setFileSizeBytes(dto.getFile().getSize());
            message.setFileMimeType(dto.getFile().getContentType());
        }

        Message updatedMessage = messageRepository.save(message);
        log.info("Обновлено сообщение с ID: {}", messageId);

        return updatedMessage;
    }
}
