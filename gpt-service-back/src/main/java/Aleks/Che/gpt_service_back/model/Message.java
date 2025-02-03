package Aleks.Che.gpt_service_back.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private Integer tokensCount;
    private LocalDateTime createdAt;
    private String filePath;
    private Long fileSizeBytes;
    private String fileMimeType;
}
