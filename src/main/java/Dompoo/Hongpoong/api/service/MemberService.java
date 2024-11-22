package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditDto;
import Dompoo.Hongpoong.api.dto.member.request.PasswordResetRequest;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.EditRoleToAdminException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import jakarta.validation.Valid;
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
    public void editMyMember(Long memberId, MemberEditDto dto, String password) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!encoder.matches(password, member.getPassword())) {
            throw new EditFailException();
        }
        
        member.edit(dto, encoder);
    }

    @Transactional
    public void deleteMemberByAdmin(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMember() {
        List<Member> members = memberRepository.findAll();
        
        return MemberResponse.fromList(members);
    }
    
    @Transactional
    public void editMemberAuth(Long myMemberId, Long memberId, MemberRoleEditDto dto) {
        Member targetMember = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        Member me = memberRepository.findById(myMemberId).orElseThrow(MemberNotFound::new);
        
        if (me.getClub() != targetMember.getClub()) {
            throw new EditFailException();
        }
        
        if (dto.getRole() == Role.ADMIN) {
            throw new EditRoleToAdminException();
        }
        
        targetMember.editRole(dto);
    }
    
    @Transactional
    public void deleteMember(Long myMemberId, Long memberId) {
        Member targetMember = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        Member me = memberRepository.findById(myMemberId).orElseThrow(MemberNotFound::new);
        
        if (me.getClub() != targetMember.getClub()) {
            throw new DeleteFailException();
        }
        
        memberRepository.delete(targetMember);
    }
    
    @Transactional
    public void editMemberAuthByAdmin(Long id, MemberRoleEditDto dto) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFound::new);

        member.editRole(dto);
    }
    
    @Transactional(readOnly = true)
    public MemberStatusResponse findMyMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(member);
    }
    
    @Transactional
    public void resetPasswordByAdmin(Long memberId, @Valid PasswordResetRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        member.resetPassword(request.getNewPassword(), encoder);
    }
}
