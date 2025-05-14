package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.ChatRepository;
import Aleks.Che.gpt_service_back.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ModelUsageAnalyzerService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Scheduled(cron = "0 0 3 * * *") // Запуск каждый день в 3 часа ночи
    public void analyzeMostUsedModels() {
        List<User> users = userRepository.findAll();
        
        for (User user : users) {
            // Здесь нужно добавить логику подсчета самой используемой модели
            // на основе истории использования из таблицы t_chat
            Long mostUsedModelId = chatRepository.findMostUsedModelByUserId(user.getId());
            user.setMostUseLlm(mostUsedModelId);
            userRepository.save(user);
        }
    }
}
