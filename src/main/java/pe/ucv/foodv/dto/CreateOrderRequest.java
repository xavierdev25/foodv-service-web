package pe.ucv.foodv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.ucv.foodv.model.entity.Order;
import jakarta.validation.constraints.AssertTrue;

@Data
public class CreateOrderRequest {

    private String pabellon;
    private String piso;
    private String salon;

    @NotNull(message = "El método de entrega es obligatorio")
    private Order.DeliveryMethod deliveryMethod;

    @NotNull(message = "El método de pago es obligatorio")
    private Order.PaymentMethod paymentMethod;

    private String notes;

    @AssertTrue(message = "Pabellón, piso y salón son obligatorios para entrega a domicilio")
    private boolean isLocationValid() {
        if (deliveryMethod == Order.DeliveryMethod.MINI_DELIVERY) {
            return pabellon != null && !pabellon.isBlank()
                    && piso != null && !piso.isBlank()
                    && salon != null && !salon.isBlank();
        }
        return true; // para RECOGIDA_TIENDA
    }
}
