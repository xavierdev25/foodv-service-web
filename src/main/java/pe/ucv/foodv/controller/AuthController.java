package pe.ucv.foodv.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.dto.ApiResponse;
import pe.ucv.foodv.dto.AuthResponse;
import pe.ucv.foodv.dto.LoginRequest;
import pe.ucv.foodv.dto.RegisterRequest;
import pe.ucv.foodv.dto.RegisterUcvRequest;
import pe.ucv.foodv.dto.UserResponse;
import pe.ucv.foodv.dto.VerifyOtpRequest;
import pe.ucv.foodv.dto.LoginUsernameRequest;
import pe.ucv.foodv.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Sistema de autenticación para usuarios tradicionales y estudiantes UCV")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Operation(
        summary = "Login (correo)",
        description = "Autentica un usuario usando email y contraseña. Para usuarios registrados tradicionalmente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales de login",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Login",
                    value = "{\n  \"email\": \"usuario@ucv.edu.pe\",\n  \"password\": \"password123\"\n}"
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Login exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Respuesta Exitosa",
                    value = "{\n  \"success\": true,\n  \"message\": \"Login exitoso\",\n  \"data\": {\n    \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n    \"type\": \"Bearer\",\n    \"id\": 1,\n    \"name\": \"Usuario\",\n    \"email\": \"usuario@ucv.edu.pe\",\n    \"role\": \"CLIENTE\"\n  }\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de Autenticación",
                    value = "{\n  \"success\": false,\n  \"message\": \"Credenciales incorrectas\",\n  \"data\": null\n}"
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(
        summary = "Registro (correo)",
        description = "Registra un nuevo usuario en el sistema con email y contraseña.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de registro",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Registro",
                    value = "{\n  \"name\": \"Juan Pérez\",\n  \"email\": \"juan@ucv.edu.pe\",\n  \"password\": \"password123\"\n}"
                )
            )
        )
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(ApiResponse.success("Registro exitoso", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(
        summary = "Iniciar Registro UCV",
        description = "Inicia el proceso de registro para estudiantes UCV. Requiere username UCV y contraseña segura. Envía código OTP por email.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de registro UCV",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterUcvRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Registro UCV",
                    value = "{\n  \"username\": \"XMONTANOGA\",\n  \"password\": \"MiPassword123!\"\n}"
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Registro iniciado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Respuesta Exitosa",
                    value = "{\n  \"success\": true,\n  \"message\": \"Proceso iniciado\",\n  \"data\": \"Código de verificación enviado a XMONTANOGA@ucvvirtual.edu.pe\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Error de validación",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de Validación",
                    value = "{\n  \"success\": false,\n  \"message\": \"El nombre de usuario ya está registrado\",\n  \"data\": null\n}"
                )
            )
        )
    })
    @PostMapping("/register-ucv")
    public ResponseEntity<ApiResponse<String>> initiateUcvRegistration(@Valid @RequestBody RegisterUcvRequest registerRequest) {
        try {
            String message = authService.initiateUcvRegistration(registerRequest);
            return ResponseEntity.ok(ApiResponse.success("Proceso iniciado", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(
        summary = "Verificar OTP",
        description = "Verifica el código OTP recibido por email y completa el registro UCV.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de verificación OTP",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = VerifyOtpRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Verificación OTP",
                    value = "{\n  \"username\": \"XMONTANOGA\",\n  \"otpCode\": \"123456\"\n}"
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Registro completado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Respuesta Exitosa",
                    value = "{\n  \"success\": true,\n  \"message\": \"Registro completado exitosamente\",\n  \"data\": {\n    \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n    \"type\": \"Bearer\",\n    \"id\": 5,\n    \"name\": \"XMONTANOGA\",\n    \"email\": \"XMONTANOGA@ucvvirtual.edu.pe\",\n    \"role\": \"CLIENTE\"\n  }\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Código OTP inválido o expirado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de OTP",
                    value = "{\n  \"success\": false,\n  \"message\": \"Código OTP inválido o expirado\",\n  \"data\": null\n}"
                )
            )
        )
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyRequest) {
        try {
            AuthResponse response = authService.completeUcvRegistration(verifyRequest);
            return ResponseEntity.ok(ApiResponse.success("Registro completado exitosamente", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @Operation(
        summary = "Login (usuario)",
        description = "Autentica un usuario usando username UCV o email. Funciona tanto para usuarios UCV como tradicionales.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales de login",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginUsernameRequest.class),
                examples = @ExampleObject(
                    name = "Ejemplo de Login con Username",
                    value = "{\n  \"usernameOrEmail\": \"XMONTANOGA\",\n  \"password\": \"MiPassword123!\"\n}"
                )
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Login exitoso",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Respuesta Exitosa",
                    value = "{\n  \"success\": true,\n  \"message\": \"Login exitoso\",\n  \"data\": {\n    \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n    \"type\": \"Bearer\",\n    \"id\": 5,\n    \"name\": \"XMONTANOGA\",\n    \"email\": \"XMONTANOGA@ucvvirtual.edu.pe\",\n    \"role\": \"CLIENTE\"\n  }\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de Autenticación",
                    value = "{\n  \"success\": false,\n  \"message\": \"Usuario no encontrado\",\n  \"data\": null\n}"
                )
            )
        )
    })
    @PostMapping("/login-username")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithUsername(@Valid @RequestBody LoginUsernameRequest loginRequest) {
        try {
            AuthResponse response = authService.loginWithUsername(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}