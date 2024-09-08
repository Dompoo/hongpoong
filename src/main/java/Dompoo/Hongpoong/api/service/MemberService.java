package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.member.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.MemberStatusResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void editMyMember(Long memberId, MemberEditDto dto) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        member.edit(dto, encoder);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMember() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional
    public void editMemberAuth(Long id, MemberRoleEditRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFound::new);

        member.setRole(request.getRole());
    }

    @Transactional(readOnly = true)
    public MemberStatusResponse getMyDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(member);
    }
}
