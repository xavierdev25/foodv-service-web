package pe.ucv.foodv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    
    @NotBlank(message = "El código OTP es obligatorio")
    @Pattern(regexp = "^\\d{6}$", message = "El código OTP debe tener exactamente 6 dígitos")
    private String otpCode;
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
}

