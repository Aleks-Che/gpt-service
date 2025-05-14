package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.SubscriptionModelAccessDTO;
import Aleks.Che.gpt_service_back.model.SubscriptionModelAccess;
import Aleks.Che.gpt_service_back.service.SubscriptionModelAccessService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription-model-access")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class SubscriptionModelAccessController {
    
    private final SubscriptionModelAccessService accessService;
    
    @PostMapping
    public ResponseEntity<SubscriptionModelAccess> createAccess(@RequestBody SubscriptionModelAccessDTO accessDTO) {
        return ResponseEntity.ok(accessService.createAccess(accessDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionModelAccess> updateAccess(@PathVariable Long id, 
                                                              @RequestBody SubscriptionModelAccessDTO accessDTO) {
        return ResponseEntity.ok(accessService.updateAccess(id, accessDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccess(@PathVariable Long id) {
        accessService.deleteAccess(id);
        return ResponseEntity.noContent().build();
    }
}
