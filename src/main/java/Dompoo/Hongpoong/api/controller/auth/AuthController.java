package Dompoo.Hongpoong.api.controller.auth;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.LoginRequest;
import Dompoo.Hongpoong.api.dto.auth.request.RejectSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.LoginResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import Dompoo.Hongpoong.api.service.AuthService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {

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
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return service.login(request);
    }

    @Secured(SecurePolicy.ADMIN)
    @GetMapping("/signup")
    public List<SignUpResponse> findAllSignup() {
        return service.findAllSignup();
    }
    
    @Secured(SecurePolicy.ADMIN)
    @PostMapping("/signup/accept")
    public void acceptSignup(@RequestBody @Valid AcceptSignUpRequest request) {
        service.acceptSignUp(request);
    }

    @Secured(SecurePolicy.ADMIN)
    @PostMapping("/signup/reject")
    public void rejectSignup(@RequestBody @Valid RejectSignUpRequest request) {
        service.rejectSignUp(request);
    }
}
