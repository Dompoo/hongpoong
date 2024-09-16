package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditDto;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.EditRoleToAdminException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void editMyMember(Long memberId, MemberEditDto dto, String password) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!encoder.matches(password, member.getPassword())) {
            throw new EditFailException();
        }
        
        member.edit(dto, encoder);
    }

    @Transactional
    public void deleteMemberByAdmin(Long memberId) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        memberJpaRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMember() {
        List<Member> members = memberJpaRepository.findAll();
        
        return MemberResponse.fromList(members);
    }
    
    @Transactional
    public void editMemberAuth(Long myMemberId, Long memberId, MemberRoleEditDto dto) {
        Member targetMember = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        Member me = memberJpaRepository.findById(myMemberId).orElseThrow(MemberNotFound::new);
        
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
        Member targetMember = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        Member me = memberJpaRepository.findById(myMemberId).orElseThrow(MemberNotFound::new);
        
        if (me.getClub() != targetMember.getClub()) {
            throw new DeleteFailException();
        }
        
        memberJpaRepository.delete(targetMember);
    }
    
    @Transactional
    public void editMemberAuthByAdmin(Long id, MemberRoleEditDto dto) {
        Member member = memberJpaRepository.findById(id).orElseThrow(MemberNotFound::new);

        member.editRole(dto);
    }
    
    @Transactional(readOnly = true)
    public MemberStatusResponse findMyMemberDetail(Long memberId) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(member);
    }
}
