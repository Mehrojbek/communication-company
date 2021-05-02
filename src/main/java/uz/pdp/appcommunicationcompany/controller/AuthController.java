package uz.pdp.appcommunicationcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcommunicationcompany.payload.ApiResponse;
import uz.pdp.appcommunicationcompany.payload.LoginDto;
import uz.pdp.appcommunicationcompany.payload.RegisterDto;
import uz.pdp.appcommunicationcompany.payload.VerifyDto;
import uz.pdp.appcommunicationcompany.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody @Valid RegisterDto registerDto){
        ApiResponse apiResponse = authService.register(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PostMapping("/verify")
    public HttpEntity<?> verify(@RequestBody @Valid VerifyDto verifyDto , @RequestParam String email, @RequestParam String emailCode){
        ApiResponse apiResponse = authService.verify(verifyDto, email, emailCode);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }


    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody @Valid LoginDto loginDto){
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:401).body(apiResponse);
    }
}
