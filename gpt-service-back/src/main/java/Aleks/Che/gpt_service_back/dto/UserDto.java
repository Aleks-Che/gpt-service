package Aleks.Che.gpt_service_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@AllArgsConstructor
@Getter
@Validated
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String password;
    private String email;
    private String phoneNumber;
    private String subscription;
    private List<String> roles;
}
