package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.LlmModelDTO;
import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.LlmModelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class LlmModelService {

    private final LlmModelRepository modelRepository;
    private final SecurityService securityService;
    public LlmModel createModel(LlmModelDTO dto) {
        LlmModel model = new LlmModel();
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setModelType(dto.getModelType());
        model.setProvider(dto.getProvider());
        model.setApiEndpoint(dto.getApiEndpoint());
        model.setApiToken(dto.getApiToken());
        model.setMaxTokens(dto.getMaxTokens());
        model.setTemperature(dto.getTemperature());
        model.setActive(true);
        model.setCreatedAt(LocalDateTime.now());

        return modelRepository.save(model);
    }

    public LlmModel updateModel(Long id, LlmModelDTO dto) {
        LlmModel model = modelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Модель не найдена"));

        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setModelType(dto.getModelType());
        model.setProvider(dto.getProvider());
        model.setApiEndpoint(dto.getApiEndpoint());
        model.setApiToken(dto.getApiToken());
        model.setMaxTokens(dto.getMaxTokens());
        model.setTemperature(dto.getTemperature());

        return modelRepository.save(model);
    }

    public void deleteModel(Long id) {
        modelRepository.deleteById(id);
    }

    public LlmModel getModelById(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Модель с ID " + id + " не найдена"));
    }

    public List<LlmModelDTO> getAllModels() {
        List<LlmModelDTO> result = new ArrayList<>();
        User currentUser = securityService.getCurrentUser();
        Set<Long> favoriteIds = Optional.ofNullable(currentUser.getFavoriteLlm())
                .map(favs -> Arrays.stream(favs.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Long mostUsedLlm = currentUser.getMostUseLlm();
        Long defaultLlm = currentUser.getDefaultLlm();

        result = modelRepository.findByIsActiveTrue().stream()
                .map(model -> {
                    // Step 1: Get subscriptions with explicit fetch
                    List<Long> subscriptionIds = modelRepository.findSubscriptionIdsByModelId(model.getId());

                    return LlmModelDTO.builder()
                            .id(model.getId())
                            .name(model.getName())
                            .description(model.getDescription())
                            .modelType(model.getModelType())
                            .provider(model.getProvider())
                            .apiEndpoint(model.getApiEndpoint())
                            .apiToken(model.getApiToken())
                            .maxTokens(model.getMaxTokens())
                            .temperature(model.getTemperature())
                            .isActive(model.isActive())
                            .subscriptionIds(subscriptionIds)
                            .isFavorite(favoriteIds.contains(model.getId()))
                            .isDefault(model.getId().equals(defaultLlm))
                            .build();
                })
                .collect(Collectors.toList());
        return result;
    }
}
