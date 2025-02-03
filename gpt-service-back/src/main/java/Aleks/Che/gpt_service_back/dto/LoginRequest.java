package Aleks.Che.gpt_service_back.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
