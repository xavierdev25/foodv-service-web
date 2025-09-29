package pe.ucv.foodv.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    
    public String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }
    
    public void storeOtp(String username, String otp, String email, String password) {
        OtpData otpData = new OtpData(otp, email, password, LocalDateTime.now().plusMinutes(10));
        otpStorage.put(username, otpData);
        logger.info("OTP almacenado para usuario: {}, expira en: {}", username, otpData.getExpiresAt());
    }
    
    public boolean verifyOtp(String username, String otp) {
        logger.info("Verificando OTP para usuario: {}, código: {}", username, otp);
        
        OtpData otpData = otpStorage.get(username);
        if (otpData == null) {
            logger.warn("No se encontró OTP para usuario: {}", username);
            return false;
        }
        
        logger.info("OTP encontrado, expira en: {}, ahora es: {}", otpData.getExpiresAt(), LocalDateTime.now());
        
        if (LocalDateTime.now().isAfter(otpData.getExpiresAt())) {
            logger.warn("OTP expirado para usuario: {}", username);
            otpStorage.remove(username);
            return false;
        }
        
        if (otpData.getOtp().equals(otp)) {
            logger.info("OTP válido para usuario: {}", username);
            return true;
        }
        
        logger.warn("OTP incorrecto para usuario: {}, esperado: {}, recibido: {}", username, otpData.getOtp(), otp);
        return false;
    }
    
    public String getEmailForUsername(String username) {
        OtpData otpData = otpStorage.get(username);
        return otpData != null ? otpData.getEmail() : null;
    }
    
    public String getPasswordForUsername(String username) {
        OtpData otpData = otpStorage.get(username);
        return otpData != null ? otpData.getPassword() : null;
    }
    
    public void removeOtp(String username) {
        otpStorage.remove(username);
        logger.info("OTP removido para usuario: {}", username);
    }
    
    // Método para debug - ver todos los OTPs almacenados
    public void debugOtpStorage() {
        logger.info("OTPs almacenados: {}", otpStorage.keySet());
        for (Map.Entry<String, OtpData> entry : otpStorage.entrySet()) {
            logger.info("Usuario: {}, OTP: {}, Expira: {}", 
                entry.getKey(), entry.getValue().getOtp(), entry.getValue().getExpiresAt());
        }
    }
    
    private static class OtpData {
        private final String otp;
        private final String email;
        private final String password;
        private final LocalDateTime expiresAt;
        
        public OtpData(String otp, String email, String password, LocalDateTime expiresAt) {
            this.otp = otp;
            this.email = email;
            this.password = password;
            this.expiresAt = expiresAt;
        }
        
        public String getOtp() { return otp; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
    }
}