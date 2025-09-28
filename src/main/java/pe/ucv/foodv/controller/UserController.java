package pe.ucv.foodv.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.dto.ApiResponse;
import pe.ucv.foodv.dto.UserResponse;
import pe.ucv.foodv.service.AuthService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UserController {
    
    @Autowired
    private AuthService authService;
    
    @Operation(summary = "Obtener perfil del usuario", description = "Retorna la información del usuario autenticado")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        try {
            UserResponse user = authService.getCurrentUser();
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

