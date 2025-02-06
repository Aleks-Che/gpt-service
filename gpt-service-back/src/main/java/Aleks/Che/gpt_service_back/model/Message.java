package Aleks.Che.gpt_service_back.model;

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
    private Chat chat;
    
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private Integer tokensCount;
    private Timestamp createdAt;
    private String filePath;
    private Long fileSizeBytes;
    private String fileMimeType;
}
