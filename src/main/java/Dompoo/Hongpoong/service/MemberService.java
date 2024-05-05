package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.exception.PasswordNotSame;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.member.MemberEditRequest;
import Dompoo.Hongpoong.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.response.MemberResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;

    public void editMember(Long memberId, MemberEditRequest request) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        if (request.getUsername() != null) member.setUsername(request.getUsername());
        if (request.getPassword1() != null && request.getPassword2() != null) {
            if(!request.getPassword1().equals(request.getPassword2())) throw new PasswordNotSame();
            member.setPassword(encoder.encode(request.getPassword1()));
        }
    }

    public void deleteMember(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        repository.delete(member);
    }

    public List<MemberResponse> getList() {
        return repository.findAll().stream()
                .map(MemberResponse::new)
                .collect(toList());
    }

    public void editRole(Long id, MemberRoleEditRequest request) {
        Member member = repository.findById(id)
                .orElseThrow(MemberNotFound::new);

        if (request.isAdmin()) {
            member.setRole(Member.Role.ROLE_ADMIN);
        } else {
            member.setRole(Member.Role.ROLE_USER);
        }
    }

    public MemberResponse getStatus(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return new MemberResponse(member);
    }
}
