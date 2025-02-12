package Aleks.Che.gpt_service_back.dto.chat;

import Aleks.Che.gpt_service_back.dto.MessageDTO;
import Aleks.Che.gpt_service_back.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long id;
    private String title;
    private Long modelId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MessageDTO> messages;

    public ChatDTO(Chat chat) {
        this.id = chat.getId();
        this.title = chat.getTitle();
        this.modelId = chat.getModel().getId();
        this.userId = chat.getUser().getId();
        this.createdAt = chat.getCreatedAt();
        this.updatedAt = chat.getUpdatedAt();
        this.messages = chat.getMessages().stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }

}
