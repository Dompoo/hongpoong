package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditDto;
import Dompoo.Hongpoong.api.dto.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.member.MemberStatusResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;

    public void editMember(Long memberId, MemberEditDto dto) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        member.edit(dto, encoder);
    }

    public void deleteMember(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        repository.delete(member);
    }

    public List<MemberResponse> getList() {
        return repository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public void editRole(Long id, MemberRoleEditRequest request) {
        Member member = repository.findById(id)
                .orElseThrow(MemberNotFound::new);

        member.setRole(request.getRole());
    }

    public MemberStatusResponse getStatus(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(member);
    }
}
