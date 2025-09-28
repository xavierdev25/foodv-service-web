package pe.ucv.foodv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
    
    private Long id;
    private String name;
    private String type;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

