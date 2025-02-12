package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.model.message.MessageType;
import Aleks.Che.gpt_service_back.model.ModelProvider;
import Aleks.Che.gpt_service_back.model.ModelType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDictionaries() {
        Map<String, Object> dictionaries = new HashMap<>();
        
        dictionaries.put("messageTypes", MessageType.values());
        dictionaries.put("modelTypes", ModelType.values());
        dictionaries.put("modelProviders", ModelProvider.values());
        
        return ResponseEntity.ok(dictionaries);
    }
}
