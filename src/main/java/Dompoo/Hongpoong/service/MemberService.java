package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditDto;
import Dompoo.Hongpoong.api.dto.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.member.MemberStatusResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public void editMember(Long memberId, MemberEditDto dto) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        member.edit(dto, encoder);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        repository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getList() {
        return repository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional
    public void editRole(Long id, MemberRoleEditRequest request) {
        Member member = repository.findById(id)
                .orElseThrow(MemberNotFound::new);

        member.setRole(request.getRole());
    }

    @Transactional(readOnly = true)
    public MemberStatusResponse getStatus(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(member);
    }
}
