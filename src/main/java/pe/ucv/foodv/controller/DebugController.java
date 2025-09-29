package pe.ucv.foodv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.ucv.foodv.service.OtpService;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = "*")
public class DebugController {
    
    @Autowired
    private OtpService otpService;
    
    @GetMapping("/otp-status")
    public String getOtpStatus() {
        otpService.debugOtpStorage();
        return "Revisa los logs para ver el estado de los OTPs";
    }
}

