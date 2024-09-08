package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.request.auth.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.request.auth.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.request.auth.LoginRequest;
import Dompoo.Hongpoong.api.dto.request.auth.SignUpRequest;
import Dompoo.Hongpoong.api.dto.response.auth.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.response.auth.LoginResponse;
import Dompoo.Hongpoong.api.dto.response.auth.SignUpResponse;
import Dompoo.Hongpoong.common.exception.impl.AlreadyExistEmail;
import Dompoo.Hongpoong.common.exception.impl.LoginFailException;
import Dompoo.Hongpoong.common.exception.impl.SignUpNotFound;
import Dompoo.Hongpoong.common.security.JwtProvider;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.SignUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final SignUpRepository signUpRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public EmailValidResponse checkEmailValid(EmailValidRequest request) {
        return EmailValidResponse.builder()
                .valid(isValidEmail(request.getEmail()))
                .build();
    }
    
    @Transactional
    public void requestSignup(SignUpRequest request) {
        if (!isValidEmail(request.getEmail())) {
            throw new AlreadyExistEmail();
        }

        signUpRepository.save(request.toSignUp(encoder));
    }
    
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginFailException::new);
        
        if (!encoder.matches(request.getPassword(), member.getPassword()))
            throw new LoginFailException();
        
        String token = jwtProvider.generateAccessToken(member.getId(), member.getEmail());
        
        return LoginResponse.builder()
                .token(token)
                .build();
    }
    
    @Transactional
    public void acceptSignUp(AcceptSignUpRequest request) {
        SignUp signUp = signUpRepository.findById(request.getEmailId())
                .orElseThrow(SignUpNotFound::new);

        if (request.isAcceptResult()) {
            memberRepository.save(Member.from(signUp));
        }

        signUpRepository.delete(signUp);
    }
    
    @Transactional(readOnly = true)
    public List<SignUpResponse> getSignUp() {
        return signUpRepository.findAll().stream()
                .map(SignUpResponse::from)
                .toList();
    }
    
    private boolean isValidEmail(String email) {
        return !signUpRepository.existsByEmail(email)
                && !memberRepository.existsByEmail(email);
    }
}
