package Aleks.Che.gpt_service_back.dto;

import Aleks.Che.gpt_service_back.model.user.User;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}
