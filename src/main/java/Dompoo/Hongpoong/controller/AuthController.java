package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.request.auth.AcceptSignUpRequest;
import Dompoo.Hongpoong.request.auth.EmailValidRequest;
import Dompoo.Hongpoong.request.auth.SignUpRequest;
import Dompoo.Hongpoong.response.auth.EmailValidResponse;
import Dompoo.Hongpoong.response.auth.SignUpResponse;
import Dompoo.Hongpoong.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signup/accept")
    public void acceptSignup(@RequestBody @Valid AcceptSignUpRequest request) {
        service.acceptSignUp(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/signup")
    public List<SignUpResponse> emailRequestList() {
        return service.getSignUp();
    }
}
