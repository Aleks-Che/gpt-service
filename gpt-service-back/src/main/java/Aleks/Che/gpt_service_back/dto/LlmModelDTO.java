package Aleks.Che.gpt_service_back.dto;

import Aleks.Che.gpt_service_back.model.ModelProvider;
import Aleks.Che.gpt_service_back.model.ModelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlmModelDTO {
    private Long id;
    private String name;
    private String description;
    private ModelType modelType;
    private ModelProvider provider;
    private String apiEndpoint;
    private String apiToken;
    private Integer maxTokens;
    private BigDecimal temperature;
    private boolean isActive;
    private List<Long> subscriptionIds;
    private Boolean isFavorite;
    private Boolean isDefault;
}
