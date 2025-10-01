package pe.ucv.foodv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.ucv.foodv.model.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal total;
    private String pabellon;
    private String piso;
    private String salon;
    private Order.DeliveryMethod deliveryMethod;
    private Order.PaymentMethod paymentMethod;
    private String notes;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

