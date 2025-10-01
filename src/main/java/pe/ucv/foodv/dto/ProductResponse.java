package pe.ucv.foodv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.ucv.foodv.model.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Product.ProductCategory category;
    private Boolean isActive;
    private Long storeId;
    private String storeName;
    private LocalDateTime createdAt;
}

