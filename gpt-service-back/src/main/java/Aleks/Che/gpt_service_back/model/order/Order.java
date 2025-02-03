package Aleks.Che.gpt_service_back.model.order;

import Aleks.Che.gpt_service_back.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "t_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    private Timestamp dateRegistration;
    private Timestamp dateReceipt;
    private Integer totalPrice;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderProduct> orderProducts;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
