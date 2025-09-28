package pe.ucv.foodv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {
    
    @NotBlank(message = "El pabellón es obligatorio")
    private String pabellon;
    
    @NotBlank(message = "El piso es obligatorio")
    private String piso;
    
    @NotBlank(message = "El salón es obligatorio")
    private String salon;
    
    private String notes;
}

