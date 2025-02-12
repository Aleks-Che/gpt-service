package Aleks.Che.gpt_service_back.model.message;

import Aleks.Che.gpt_service_back.model.Chat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "t_message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private Chat chat;
    
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer tokensCount;

    @Column(columnDefinition = "TEXT")
    private String contentSummarize;

    private Integer summarizeTokensCount;

    private Timestamp createdAt;
    private String filePath;
    private Long fileSizeBytes;
    private String fileMimeType;
}
