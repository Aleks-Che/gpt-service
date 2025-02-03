package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.SubscriptionModelAccessDTO;
import Aleks.Che.gpt_service_back.model.SubscriptionModelAccess;
import Aleks.Che.gpt_service_back.repository.SubscriptionModelAccessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class SubscriptionModelAccessService {
    
    private final SubscriptionModelAccessRepository accessRepository;
    
    public SubscriptionModelAccess createAccess(SubscriptionModelAccessDTO dto) {
        SubscriptionModelAccess access = new SubscriptionModelAccess();
        access.setSubscription(dto.getSubscription());
        access.setModel(dto.getModel());
        access.setMonthlyRequestLimit(dto.getMonthlyRequestLimit());
        access.setMaxTokensPerRequest(dto.getMaxTokensPerRequest());
        access.setMaxFileSizeMb(dto.getMaxFileSizeMb());
        
        return accessRepository.save(access);
    }
    
    public SubscriptionModelAccess updateAccess(Long id, SubscriptionModelAccessDTO dto) {
        SubscriptionModelAccess access = accessRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Настройки доступа не найдены"));
            
        access.setMonthlyRequestLimit(dto.getMonthlyRequestLimit());
        access.setMaxTokensPerRequest(dto.getMaxTokensPerRequest());
        access.setMaxFileSizeMb(dto.getMaxFileSizeMb());
        
        return accessRepository.save(access);
    }

    public void deleteAccess(Long id) {
        SubscriptionModelAccess access = accessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Настройки доступа с ID " + id + " не найдены"));

        accessRepository.deleteById(id);
        log.info("Удалены настройки доступа с ID: {}", id);
    }
}
