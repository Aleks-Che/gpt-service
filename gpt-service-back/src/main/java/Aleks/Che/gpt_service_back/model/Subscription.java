package Aleks.Che.gpt_service_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "t_subscription")
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDays;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "subscription")
    @JsonIgnore
    private Set<SubscriptionModelAccess> modelAccesses;
}
