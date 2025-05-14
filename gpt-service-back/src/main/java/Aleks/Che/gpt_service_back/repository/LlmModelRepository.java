package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.model.ModelProvider;
import Aleks.Che.gpt_service_back.model.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LlmModelRepository extends JpaRepository<LlmModel, Long> {
    List<LlmModel> findByIsActiveTrue();
    List<LlmModel> findByModelType(ModelType modelType);
    List<LlmModel> findByProvider(ModelProvider provider);
    Optional<LlmModel> findByNameIgnoreCase(String name);
    @Query("SELECT sa.subscription.id FROM SubscriptionModelAccess sa WHERE sa.model.id = :modelId")
    List<Long> findSubscriptionIdsByModelId(@Param("modelId") Long modelId);
}
