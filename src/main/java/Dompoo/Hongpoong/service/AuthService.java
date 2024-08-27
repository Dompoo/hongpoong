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
import Dompoo.Hongpoong.domain.Setting;
import Dompoo.Hongpoong.domain.SignUp;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SettingRepository;
import Dompoo.Hongpoong.repository.SignUpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final SignUpRepository signUpRepository;
    private final PasswordEncoder encoder;
    private final SettingRepository settingRepository;

    public EmailValidResponse checkEmailValid(EmailValidRequest request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        Optional<SignUp> findSignUp = signUpRepository.findByEmail(request.getEmail());
        
        boolean valid = findMember.isEmpty() && findSignUp.isEmpty();

        return EmailValidResponse.builder()
                .valid(valid)
                .build();
    }

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

    public void acceptSignUp(AcceptSignUpRequest request) {
        SignUp signUp = signUpRepository.findById(request.getEmailId())
                .orElseThrow(SignUpNotFound::new);

        if (request.isAcceptResult()) {
            Member member = memberRepository.save(Member.from(signUp));

            settingRepository.save(Setting.builder()
                    .member(member)
                    .build());
        }

        signUpRepository.delete(signUp);
    }

    public List<SignUpResponse> getSignUp() {
        return signUpRepository.findAll().stream()
                .map(SignUpResponse::from)
                .toList();
    }
}
