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
import pe.ucv.foodv.dto.UserResponse;
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
}

