package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.auth.*;
import Dompoo.Hongpoong.api.service.AuthService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/email")
    public EmailValidResponse checkEmailValid(@RequestBody @Valid EmailValidRequest request) {
        return service.checkEmailValid(request);
    }

    @PostMapping("/signup")
    public void requestSignup(@RequestBody @Valid SignUpRequest request) {
        service.requestSignup(request);
    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PostMapping("/signup/accept")
    public void acceptSignup(@RequestBody @Valid AcceptSignUpRequest request) {
        service.acceptSignUp(request);
    }
    
    @Secured(SecurePolicy.ADMIN_ONLY)
    @GetMapping("/signup")
    public List<SignUpResponse> emailRequestList() {
        return service.getSignUp();
    }
}
