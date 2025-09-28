package pe.ucv.foodv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    
    private Long id;
    private Long userId;
    private BigDecimal total;
    private List<CartItemResponse> items;
    private LocalDateTime updatedAt;
}

