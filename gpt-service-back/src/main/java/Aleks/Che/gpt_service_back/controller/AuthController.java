package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.AuthResponse;
import Aleks.Che.gpt_service_back.dto.LoginRequest;
import Aleks.Che.gpt_service_back.dto.UserDto;
import Aleks.Che.gpt_service_back.service.AuthService;
import Aleks.Che.gpt_service_back.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        authService.register(userDto);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
