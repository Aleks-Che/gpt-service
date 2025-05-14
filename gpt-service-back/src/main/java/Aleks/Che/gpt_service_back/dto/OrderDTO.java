package Aleks.Che.gpt_service_back.dto;

import Aleks.Che.gpt_service_back.model.order.ProductInOrder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private LocalDateTime dateRegistration;
    private LocalDateTime dateReceipt;
    private Integer totalPrice;
    private String status;
    private Long userId;
    private List<ProductInOrder> products;
}

