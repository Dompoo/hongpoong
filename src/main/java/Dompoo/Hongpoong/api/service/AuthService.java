package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.auth.request.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.request.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.auth.request.LoginRequest;
import Dompoo.Hongpoong.api.dto.auth.request.SignUpRequest;
import Dompoo.Hongpoong.api.dto.auth.response.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.auth.response.LoginResponse;
import Dompoo.Hongpoong.api.dto.auth.response.SignUpResponse;
import Dompoo.Hongpoong.common.exception.impl.AlreadyExistEmail;
import Dompoo.Hongpoong.common.exception.impl.LoginFailException;
import Dompoo.Hongpoong.common.exception.impl.SignUpNotFound;
import Dompoo.Hongpoong.common.security.JwtProvider;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.SignUpJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberJpaRepository memberJpaRepository;
    private final SignUpJpaRepository signUpJpaRepository;
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

        signUpJpaRepository.save(request.toSignUp(encoder));
    }
    
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Member member = memberJpaRepository.findByEmail(request.getEmail())
                .orElseThrow(LoginFailException::new);
        
        if (!encoder.matches(request.getPassword(), member.getPassword()))
            throw new LoginFailException();
        
        String token = jwtProvider.generateAccessToken(member.getId(), member.getEmail(), member.getRole(), member.getClub());
        
        return LoginResponse.builder()
                .token(token)
                .build();
    }
    
    @Transactional
    public void acceptSignUp(Long signupId, AcceptSignUpRequest request) {
        SignUp signUp = signUpJpaRepository.findById(signupId)
                .orElseThrow(SignUpNotFound::new);

        if (request.getAcceptResult()) {
            memberJpaRepository.save(Member.from(signUp));
        }

        signUpJpaRepository.delete(signUp);
    }
    
    @Transactional(readOnly = true)
    public List<SignUpResponse> findAllSignup() {
        return signUpJpaRepository.findAll().stream()
                .map(SignUpResponse::from)
                .toList();
    }
    
    private boolean isValidEmail(String email) {
        return !signUpJpaRepository.existsByEmail(email)
                && !memberJpaRepository.existsByEmail(email);
    }
}
