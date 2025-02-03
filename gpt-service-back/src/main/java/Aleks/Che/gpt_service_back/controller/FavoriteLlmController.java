package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.service.FavoriteLlmService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite-llm")
@AllArgsConstructor
public class FavoriteLlmController {
    private final FavoriteLlmService favoriteLlmService;

    @PostMapping("/{modelId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long modelId) {
        favoriteLlmService.addToFavorites(modelId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Long>> getFavorites() {
        return ResponseEntity.ok(favoriteLlmService.getFavorites());
    }

    @DeleteMapping("/{modelId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long modelId) {
        favoriteLlmService.removeFromFavorites(modelId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/default/{modelId}")
    public ResponseEntity<Void> setDefaultModel(@PathVariable Long modelId) {
        favoriteLlmService.setDefaultModel(modelId);
        return ResponseEntity.ok().build();
    }

}
