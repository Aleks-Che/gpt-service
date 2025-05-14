package Aleks.Che.gpt_service_back.repository;

import Aleks.Che.gpt_service_back.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByIsActiveTrue();
    Optional<Subscription> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
