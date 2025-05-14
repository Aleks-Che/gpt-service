package Aleks.Che.gpt_service_back.model.user;

import Aleks.Che.gpt_service_back.model.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String phoneNumber;
    private Boolean phoneNumberConfirm;
    private String email;
    private Boolean emailConfirm;
    private Boolean isActive;
    private String favoriteLlm;
    private Long mostUseLlm;
    private Long defaultLlm;
    private String subscription;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Order> orders;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
