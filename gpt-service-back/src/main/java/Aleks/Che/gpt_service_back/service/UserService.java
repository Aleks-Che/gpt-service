package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.dto.UserDto;
import Aleks.Che.gpt_service_back.model.user.Role;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
        log.debug("User found: {}", user.getUsername());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    private List<? extends GrantedAuthority> mapRolesToAthorities(Set<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList());
    }

    public void createUser(UserDto userRq) throws Exception {
        boolean userExists = userRepository.existsByUsername(userRq.getUsername());
        if (userExists) {
            throw new Exception("user exist");
        }
        if (userRq.getUsername() == null) {
            throw new Exception("username must not be null");
        }
        User user = new User();
        user.setUsername(userRq.getUsername());
        user.setFirstName(userRq.getFirstName());
        user.setLastName(userRq.getLastName());
        user.setCity(userRq.getCity());
        user.setPassword(passwordEncoder.encode(userRq.getPassword()));
        user.setEmail(userRq.getEmail());
        user.setPhoneNumber(userRq.getPhoneNumber());
        user.setRoles(Collections.singleton(Role.USER));
        user.setIsActive(true);
        userRepository.save(user);
    }

    public User editUser(UserDto userRq, Long id) throws Exception {
        User editUser = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User with id " + id + " not found"));
        String encodePassword = passwordEncoder.encode(userRq.getPassword());
        editUser.setPassword(encodePassword);
        editUser.setEmail(userRq.getEmail());
        editUser.setEmail(userRq.getEmail());
        editUser.setPhoneNumber(userRq.getPhoneNumber());
        return userRepository.save(editUser);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Current authentication: {}", auth);
        log.debug("Authentication principal: {}", auth.getPrincipal());
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> {
                    log.error("Current user not found: {}", auth.getName());
                    return new UsernameNotFoundException("Current user not found");
                });
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public Authentication getAuthenticationInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
