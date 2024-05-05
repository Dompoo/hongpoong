package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.request.auth.AcceptEmailRequest;
import Dompoo.Hongpoong.request.auth.AddEmailRequest;
import Dompoo.Hongpoong.request.auth.SignupRequest;
import Dompoo.Hongpoong.response.EmailResponse;
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

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid SignupRequest request) {
        service.signup(request);
    }

    @PostMapping("/email")
    public void requestEmail(@RequestBody @Valid AddEmailRequest request) {
        service.addWhiteList(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/email/accept")
    public void acceptEmail(@RequestBody @Valid AcceptEmailRequest request) {
        service.acceptWhiteList(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/email")
    public List<EmailResponse> emailRequestList() {
        return service.getWhiteList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/email/{id}")
    public void deleteEmail(@PathVariable Long id) {
        service.deleteWhiteList(id);
    }
}
