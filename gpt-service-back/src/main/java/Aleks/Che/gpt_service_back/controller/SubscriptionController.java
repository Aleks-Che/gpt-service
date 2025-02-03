package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.SubscriptionDTO;
import Aleks.Che.gpt_service_back.model.Subscription;
import Aleks.Che.gpt_service_back.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    
    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.ok(subscriptionService.createSubscription(subscriptionDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, 
                                                         @RequestBody SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, subscriptionDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }
}
