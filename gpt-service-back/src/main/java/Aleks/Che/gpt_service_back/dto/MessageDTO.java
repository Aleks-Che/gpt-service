package Aleks.Che.gpt_service_back.dto;

import Aleks.Che.gpt_service_back.model.MessageType;
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
    private Long conversationId;
    private MessageType messageType;
    private String content;
    private Integer tokensCount;
    private LocalDateTime createdAt;
    private String filePath;
    private Long fileSizeBytes;
    private String fileMimeType;
    private MultipartFile file;
}
