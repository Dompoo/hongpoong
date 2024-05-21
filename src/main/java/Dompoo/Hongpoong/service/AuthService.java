package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Member.Club;
import Dompoo.Hongpoong.domain.Setting;
import Dompoo.Hongpoong.domain.SignUp;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SettingRepository;
import Dompoo.Hongpoong.repository.SignUpRepository;
import Dompoo.Hongpoong.request.auth.AcceptSignUpRequest;
import Dompoo.Hongpoong.request.auth.EmailValidRequest;
import Dompoo.Hongpoong.request.auth.SignUpRequest;
import Dompoo.Hongpoong.response.auth.SignUpResponse;
import Dompoo.Hongpoong.response.auth.EmailValidResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final SignUpRepository signUpRepository;
    private final PasswordEncoder passwordEncoder;
    private final SettingRepository settingRepository;

    public EmailValidResponse checkEmailValid(@Valid EmailValidRequest request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        Optional<SignUp> findSignUp = signUpRepository.findByEmail(request.getEmail());

        boolean valid = true;

        if (findMember.isPresent()) valid = false;
        if (findSignUp.isPresent()) valid = false;

        return EmailValidResponse.builder()
                .valid(valid)
                .build();
    }

    public void requestSignup(SignUpRequest request) {
        if (!Objects.equals(request.getPassword1(), request.getPassword2())) {
            throw new PasswordNotSame();
        }

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistEmail();
        }

        if (signUpRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistEmail();
        }

        signUpRepository.save(SignUp.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword1()))
                .club(Club.byInt(request.getClub()))
                .build());
    }

    public void acceptSignUp(AcceptSignUpRequest request) {
        SignUp signUp = signUpRepository.findById(request.getEmailId())
                .orElseThrow(SignUpNotFound::new);

        if (request.isAcceptResult()) {
            Member member = memberRepository.save(new Member(signUp));

            settingRepository.save(Setting.builder()
                    .member(member)
                    .build());
        }

        signUpRepository.delete(signUp);
    }

    public List<SignUpResponse> getSignUp() {
        return signUpRepository.findAll()
                .stream().map(SignUpResponse::new)
                .collect(Collectors.toList());
    }
}
