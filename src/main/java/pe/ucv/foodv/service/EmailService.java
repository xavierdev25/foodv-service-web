package pe.ucv.foodv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.mail.from:noreply@foodv.ucv.edu.pe}")
    private String fromEmail;
    
    public void sendOtpEmail(String toEmail, String otpCode, String username) {
        try {
            logger.info("Intentando enviar email a: {}", toEmail);
            logger.info("From email: {}", fromEmail);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Código de Verificación - FoodV UCV");
            
            String emailBody = String.format("""
                Hola %s,
                
                Tu código de verificación para completar el registro en FoodV es:
                
                %s
                
                Este código expira en 10 minutos.
                
                Si no solicitaste este registro, puedes ignorar este correo.
                
                ¡Bienvenido a FoodV!
                Equipo FoodV UCV
                """, username, otpCode);
            
            message.setText(emailBody);
            
            logger.info("Enviando email...");
            mailSender.send(message);
            logger.info("Email enviado exitosamente a: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Error al enviar email a {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }
}