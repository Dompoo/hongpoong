package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.exception.PasswordNotSame;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.member.MemberEditRequest;
import Dompoo.Hongpoong.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.response.member.MemberListResponse;
import Dompoo.Hongpoong.response.member.MemberStatusResponse;
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

    public List<MemberListResponse> getList() {
        return repository.findAll().stream()
                .map(MemberListResponse::new)
                .collect(toList());
    }

    public void editRole(Long id, MemberRoleEditRequest request) {
        Member member = repository.findById(id)
                .orElseThrow(MemberNotFound::new);

        member.setRole(request.getRole());
    }

    public MemberStatusResponse getStatus(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return new MemberStatusResponse(member);
    }
}
