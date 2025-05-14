package Aleks.Che.gpt_service_back.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewChatRequest {
    private String message;
    private Long modelId;
}

