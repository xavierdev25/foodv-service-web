package pe.ucv.foodv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @NotNull(message = "La cantidad es obligatoria")
    private Integer quantity;
}

