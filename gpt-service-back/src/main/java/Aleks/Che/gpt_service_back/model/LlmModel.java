package Aleks.Che.gpt_service_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "t_llm_model")
@Data
public class LlmModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ModelType modelType;
    
    @Enumerated(EnumType.STRING)
    private ModelProvider provider;
    
    private String apiEndpoint;
    private String apiToken;
    private Integer maxTokens;
    private BigDecimal temperature;
    private boolean isActive;

    @OneToMany(mappedBy = "model")
    @JsonIgnore
    private Set<SubscriptionModelAccess> subscriptionAccesses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
