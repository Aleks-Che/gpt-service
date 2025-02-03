package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.SubscriptionDTO;
import Aleks.Che.gpt_service_back.model.Subscription;
import Aleks.Che.gpt_service_back.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SubscriptionService {
    
    private final SubscriptionRepository subscriptionRepository;
    
    public Subscription createSubscription(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setName(dto.getName());
        subscription.setDescription(dto.getDescription());
        subscription.setPrice(dto.getPrice());
        subscription.setDurationDays(dto.getDurationDays());
        subscription.setActive(true);
        subscription.setCreatedAt(LocalDateTime.now());
        
        return subscriptionRepository.save(subscription);
    }
    
    public Subscription updateSubscription(Long id, SubscriptionDTO dto) {
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Подписка не найдена"));
            
        subscription.setName(dto.getName());
        subscription.setDescription(dto.getDescription());
        subscription.setPrice(dto.getPrice());
        subscription.setDurationDays(dto.getDurationDays());
        
        return subscriptionRepository.save(subscription);
    }
    
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
    
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
}
