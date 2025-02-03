package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteLlmService {
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public void addToFavorites(Long modelId) {
        User user = securityService.getCurrentUser();
        String currentFavorites = user.getFavoriteLlm();
        Set<String> favoriteSet = currentFavorites == null ?
            new HashSet<>() :
            new HashSet<>(Arrays.asList(currentFavorites.split(",")));
        
        favoriteSet.add(modelId.toString());
        user.setFavoriteLlm(String.join(",", favoriteSet));
        userRepository.save(user);
    }

    public List<Long> getFavorites() {
        User user = securityService.getCurrentUser();
        if (user.getFavoriteLlm() == null) return new ArrayList<>();
        return Arrays.stream(user.getFavoriteLlm().split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    public void removeFromFavorites(Long modelId) {
        User user = securityService.getCurrentUser();
        if (user.getFavoriteLlm() == null) return;
        
        Set<String> favoriteSet = new HashSet<>(Arrays.asList(user.getFavoriteLlm().split(",")));
        favoriteSet.remove(modelId.toString());
        user.setFavoriteLlm(favoriteSet.isEmpty() ? null : String.join(",", favoriteSet));
        userRepository.save(user);
    }

    public void setDefaultModel(Long modelId) {
        User user = securityService.getCurrentUser();
        user.setDefaultLlm(modelId);
        userRepository.save(user);
    }

}
