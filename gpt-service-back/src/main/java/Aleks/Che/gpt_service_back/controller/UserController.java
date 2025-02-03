package Aleks.Che.gpt_service_back.controller;

import Aleks.Che.gpt_service_back.dto.UserDto;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public String createUser(@RequestBody UserDto user) {
        try {
            userService.createUser(user);
            return user.getUsername() + " is registered";
        } catch (Exception e) {
            return "registration failed. Because " + e.getLocalizedMessage();
        }
    }

    @GetMapping("/get/{username}")
    public User getByUsername (@PathVariable String username) {
            return userService.getByUsername(username);
    }

    @GetMapping("/current")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("/user/{id}")
    public Object editUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        try {
            return userService.editUser(userDto, id);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
    }

    @DeleteMapping("/api/user/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "{\"state\":\"success\"}";
    }
}
