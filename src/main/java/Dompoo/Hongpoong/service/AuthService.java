package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Setting;
import Dompoo.Hongpoong.domain.Whitelist;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.SettingRepository;
import Dompoo.Hongpoong.repository.WhitelistRepository;
import Dompoo.Hongpoong.request.auth.AcceptEmailRequest;
import Dompoo.Hongpoong.request.auth.AddEmailRequest;
import Dompoo.Hongpoong.request.auth.SignupRequest;
import Dompoo.Hongpoong.response.EmailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository repository;
    private final WhitelistRepository whitelistRepository;
    private final PasswordEncoder passwordEncoder;
    private final SettingRepository settingRepository;

    public void signup(SignupRequest request) {
        if (!Objects.equals(request.getPassword1(), request.getPassword2())) {
            throw new PasswordNotSame();
        }

        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistsUsername();
        }

        Whitelist whitelist = whitelistRepository.findByEmail(request.getEmail())
                .orElseThrow(NotInWhitelist::new);

        if (!whitelist.getIsAccepted()) {
            throw new NotAcceptedMember();
        }

        if (whitelist.getIsUsed()) {
            throw new AlreadyUsedEmail();
        }

        Member member = repository.save(Member.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword1()))
                .club(request.getClub())
                .build());

        settingRepository.save(Setting.builder()
                .member(member)
                .build());

        whitelist.setIsUsed(true);
    }

    public void addWhiteList(AddEmailRequest request) {
        if(whitelistRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistsEmail();
        }

        whitelistRepository.save(Whitelist.builder()
                .email(request.getEmail())
                .isAccepted(false)
                .build());
    }

    public void acceptWhiteList(AcceptEmailRequest request) {
        Whitelist whitelist = whitelistRepository.findById(request.getEmailId())
                .orElseThrow(EmailNotFound::new);
        if (request.isAcceptResult()) {
            if (whitelist.getIsAccepted()) {
                throw new AlreadyAcceptedEmail();
            }
            whitelist.setIsAccepted(true);
        } else {
            whitelistRepository.delete(whitelist);
        }
    }

    public List<EmailResponse> getWhiteList() {
        return whitelistRepository.findAll()
                .stream().map(EmailResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteWhiteList(Long id) {
        Whitelist whitelist = whitelistRepository.findById(id)
                .orElseThrow(EmailNotFound::new);

        //만약 해당 화이트리스트로 가입된 회원이 있다면 삭제
        repository.findByEmail(whitelist.getEmail())
                .ifPresent(repository::delete);

        whitelistRepository.delete(whitelist);
    }
}
