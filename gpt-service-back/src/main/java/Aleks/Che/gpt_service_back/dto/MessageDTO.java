package Aleks.Che.gpt_service_back.dto;

import Aleks.Che.gpt_service_back.model.message.Message;
import Aleks.Che.gpt_service_back.model.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private Long chatId;
    private MessageType messageType;
    private String content;
    private Integer tokensCount;
    private LocalDateTime createdAt;
    private String filePath;
    private Long fileSizeBytes;
    private String fileMimeType;
    private MultipartFile file;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.chatId = message.getChat().getId();
        this.messageType = message.getMessageType();
        this.content = message.getContent();
        this.tokensCount = message.getTokensCount();
        this.createdAt = message.getCreatedAt().toLocalDateTime();
        this.filePath = message.getFilePath();
        this.fileSizeBytes = message.getFileSizeBytes();
        this.fileMimeType = message.getFileMimeType();
    }

}
