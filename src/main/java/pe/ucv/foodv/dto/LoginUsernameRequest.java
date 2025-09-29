package pe.ucv.foodv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUsernameRequest {
    
    @NotBlank(message = "El username o email es obligatorio")
    private String usernameOrEmail;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}

