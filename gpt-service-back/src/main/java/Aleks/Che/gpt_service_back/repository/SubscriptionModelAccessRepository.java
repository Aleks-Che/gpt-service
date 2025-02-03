package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.SubscriptionModelAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionModelAccessRepository extends JpaRepository<SubscriptionModelAccess, Long> {
    List<SubscriptionModelAccess> findBySubscriptionId(Long subscriptionId);
    List<SubscriptionModelAccess> findByModelId(Long modelId);
    Optional<SubscriptionModelAccess> findBySubscriptionIdAndModelId(Long subscriptionId, Long modelId);
    void deleteBySubscriptionId(Long subscriptionId);
    void deleteByModelId(Long modelId);
}
