package Aleks.Che.gpt_service_back.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "t_subscription_model_access")
@Data
public class SubscriptionModelAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
    
    @ManyToOne
    @JoinColumn(name = "model_id")
    private LlmModel model;
    
    private Integer monthlyRequestLimit;
    private Integer maxTokensPerRequest;
    private Integer maxFileSizeMb;
}
