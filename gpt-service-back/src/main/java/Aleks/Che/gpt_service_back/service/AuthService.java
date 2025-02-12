package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.AuthResponse;
import Aleks.Che.gpt_service_back.dto.LoginRequest;
import Aleks.Che.gpt_service_back.dto.UserDto;
import Aleks.Che.gpt_service_back.model.user.Role;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.UserRepository;
import Aleks.Che.gpt_service_back.security.JwtService;
import Aleks.Che.gpt_service_back.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        String token = jwtTokenProvider.createToken(user.getUsername());
        
        return new AuthResponse(token, user);
    }
    
    public void register(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        Set roleSet = new HashSet<>();
        roleSet.add(Role.USER);
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setCity(userDto.getCity());
        user.setCountry(userDto.getCountry());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setIsActive(true);
        user.setEmailConfirm(false);
        user.setPhoneNumberConfirm(false);
        user.setDefaultLlm(2L);
        user.setSubscription("Free");
        user.setRoles(roleSet);
        userRepository.save(user);
    }

    public Authentication getAuthentication(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
