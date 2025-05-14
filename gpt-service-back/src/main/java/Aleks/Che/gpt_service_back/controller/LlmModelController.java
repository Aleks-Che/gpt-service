package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.LlmModelDTO;
import Aleks.Che.gpt_service_back.model.LlmModel;
import Aleks.Che.gpt_service_back.service.LlmModelService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class LlmModelController {
    
    private final LlmModelService modelService;
    
    @PostMapping
    public ResponseEntity<LlmModel> createModel(@RequestBody LlmModelDTO modelDTO) {
        return ResponseEntity.ok(modelService.createModel(modelDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<LlmModel> updateModel(@PathVariable Long id, 
                                              @RequestBody LlmModelDTO modelDTO) {
        return ResponseEntity.ok(modelService.updateModel(id, modelDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<List<LlmModelDTO>> getAllModels() {
        return ResponseEntity.ok(modelService.getAllModels());
    }
}
