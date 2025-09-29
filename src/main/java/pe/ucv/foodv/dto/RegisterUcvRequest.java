package pe.ucv.foodv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUcvRequest {
    
    @NotBlank(message = "El nombre de usuario UCV es obligatorio")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El nombre de usuario debe contener solo letras mayúsculas y números")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "La contraseña debe contener al menos: 8 caracteres, una letra minúscula, una mayúscula, un número y un símbolo")
    private String password;
}

