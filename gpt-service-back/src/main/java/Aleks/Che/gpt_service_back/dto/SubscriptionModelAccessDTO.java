package Aleks.Che.gpt_service_back.dto;

import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionModelAccessDTO {
    private Long id;
    private Long subscriptionId;
    private Long modelId;
    private Integer monthlyRequestLimit;
    private Integer maxTokensPerRequest;
    private Integer maxFileSizeMb;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Subscription subscription;
    private LlmModel model;
    public Subscription getSubscription() {
        return subscription;
    }

    public LlmModel getModel() {
        return model;
    }
}
