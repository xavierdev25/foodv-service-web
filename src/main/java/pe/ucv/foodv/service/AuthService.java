package pe.ucv.foodv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.ucv.foodv.dto.AuthResponse;
import pe.ucv.foodv.dto.LoginRequest;
import pe.ucv.foodv.dto.RegisterRequest;
import pe.ucv.foodv.dto.RegisterUcvRequest;
import pe.ucv.foodv.dto.UserResponse;
import pe.ucv.foodv.dto.VerifyOtpRequest;
import pe.ucv.foodv.dto.LoginUsernameRequest;
import pe.ucv.foodv.model.entity.User;
import pe.ucv.foodv.repository.UserRepository;
import pe.ucv.foodv.security.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private OtpService otpService;
    
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return new AuthResponse(jwt, "Bearer", user.getId(), user.getName(), 
                               user.getEmail(), user.getRole().name());
    }
    
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.UserRole.CLIENTE);
        
        User savedUser = userRepository.save(user);
        
        String jwt = jwtUtil.generateTokenFromUsername(savedUser.getEmail());
        
        return new AuthResponse(jwt, "Bearer", savedUser.getId(), savedUser.getName(), 
                               savedUser.getEmail(), savedUser.getRole().name());
    }
    
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), 
                               user.getRole().name(), user.getCreatedAt());
    }
    
    public String initiateUcvRegistration(RegisterUcvRequest registerRequest) {
        // Verificar si el username ya existe
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }
        
        // Generar email UCV
        String ucvEmail = registerRequest.getUsername() + "@ucvvirtual.edu.pe";
        
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(ucvEmail)) {
            throw new RuntimeException("El email UCV ya está registrado");
        }
        
        // Generar y almacenar OTP junto con la contraseña
        String otp = otpService.generateOtp();
        otpService.storeOtp(registerRequest.getUsername(), otp, ucvEmail, registerRequest.getPassword());
        
        // Enviar email con OTP
        emailService.sendOtpEmail(ucvEmail, otp, registerRequest.getUsername());
        
        return "Código de verificación enviado a " + ucvEmail;
    }
    
    public AuthResponse completeUcvRegistration(VerifyOtpRequest verifyRequest) {
        // Verificar OTP
        if (!otpService.verifyOtp(verifyRequest.getUsername(), verifyRequest.getOtpCode())) {
            throw new RuntimeException("Código OTP inválido o expirado");
        }
        
        // Obtener datos del OTP
        String ucvEmail = otpService.getEmailForUsername(verifyRequest.getUsername());
        String password = otpService.getPasswordForUsername(verifyRequest.getUsername());
        
        if (ucvEmail == null || password == null) {
            throw new RuntimeException("Sesión de registro expirada. Por favor, inicia el registro nuevamente.");
        }
        
        // Crear usuario
        User user = new User();
        user.setUsername(verifyRequest.getUsername());
        user.setName(verifyRequest.getUsername()); // Usar username como nombre por defecto
        user.setEmail(ucvEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.UserRole.CLIENTE);
        
        User savedUser = userRepository.save(user);
        
        // Limpiar OTP
        otpService.removeOtp(verifyRequest.getUsername());
        
        // Generar JWT
        String jwt = jwtUtil.generateTokenFromUsername(savedUser.getEmail());
        
        return new AuthResponse(jwt, "Bearer", savedUser.getId(), savedUser.getName(), 
                               savedUser.getEmail(), savedUser.getRole().name());
    }
    
    public AuthResponse loginWithUsername(LoginUsernameRequest loginRequest) {
        // Buscar usuario por username o email
        User user = userRepository.findByUsername(loginRequest.getUsernameOrEmail())
                .orElse(userRepository.findByEmail(loginRequest.getUsernameOrEmail())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));
        
        // Verificar contraseña
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        // Generar JWT
        String jwt = jwtUtil.generateTokenFromUsername(user.getEmail());
        
        return new AuthResponse(jwt, "Bearer", user.getId(), user.getName(), 
                               user.getEmail(), user.getRole().name());
    }
}