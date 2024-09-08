package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.member.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
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
    public List<MemberResponse> findAllMember() {
        List<Member> members = memberRepository.findAll();
        
        return MemberResponse.fromList(members);
    }

    @Transactional
    public void editMemberAuth(Long id, MemberRoleEditRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFound::new);

        member.setRole(request.getRole());
    }

    @Transactional(readOnly = true)
    public MemberStatusResponse findMyMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(member);
    }
}
