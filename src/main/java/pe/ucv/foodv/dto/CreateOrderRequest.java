package pe.ucv.foodv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.ucv.foodv.model.entity.Order;

@Data
public class CreateOrderRequest {
    
    @NotBlank(message = "El pabellón es obligatorio")
    private String pabellon;
    
    @NotBlank(message = "El piso es obligatorio")
    private String piso;
    
    @NotBlank(message = "El salón es obligatorio")
    private String salon;
    
    @NotNull(message = "El método de entrega es obligatorio")
    private Order.DeliveryMethod deliveryMethod;
    
    @NotNull(message = "El método de pago es obligatorio")
    private Order.PaymentMethod paymentMethod;
    
    private String notes;
}

