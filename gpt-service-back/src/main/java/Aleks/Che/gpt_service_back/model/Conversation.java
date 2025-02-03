package Aleks.Che.gpt_service_back.model;

import Aleks.Che.gpt_service_back.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_conversation")
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "model_id")
    private LlmModel model;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isArchived;
    
    @OneToMany(mappedBy = "conversation")
    @JsonIgnore
    private List<Message> messages;
}
