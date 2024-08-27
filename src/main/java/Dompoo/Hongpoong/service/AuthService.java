package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.auth.AcceptSignUpRequest;
import Dompoo.Hongpoong.api.dto.request.auth.EmailValidRequest;
import Dompoo.Hongpoong.api.dto.request.auth.SignUpRequest;
import Dompoo.Hongpoong.api.dto.response.auth.EmailValidResponse;
import Dompoo.Hongpoong.api.dto.response.auth.SignUpResponse;
import Dompoo.Hongpoong.common.exception.impl.AlreadyExistEmail;
import Dompoo.Hongpoong.common.exception.impl.PasswordNotSame;
import Dompoo.Hongpoong.common.exception.impl.SignUpNotFound;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.SignUp;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SignUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final SignUpRepository signUpRepository;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public EmailValidResponse checkEmailValid(EmailValidRequest request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        Optional<SignUp> findSignUp = signUpRepository.findByEmail(request.getEmail());
        
        boolean valid = findMember.isEmpty() && findSignUp.isEmpty();

        return EmailValidResponse.builder()
                .valid(valid)
                .build();
    }

    @Transactional
    public void requestSignup(SignUpRequest request) {
        if (!request.getPassword1().equals(request.getPassword2())) {
            throw new PasswordNotSame();
        }
        
        if (memberRepository.findByEmail(request.getEmail()).isPresent()
                || signUpRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistEmail();
        }

        signUpRepository.save(request.toSignUp(encoder));
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
}
