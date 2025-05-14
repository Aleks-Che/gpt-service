package Aleks.Che.gpt_service_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDays;
    private boolean isActive;
    private LocalDateTime createdAt;
    private List<Long> modelIds;
    private Integer totalAvailableModels;
    private Integer maxRequestsPerMonth;
    private Integer maxTokensPerRequest;
    private Integer maxFileSizeMb;
}
