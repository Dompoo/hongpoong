package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditDto;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberStatusResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.EditRoleToAdminException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!encoder.matches(password, memberJpaEntity.getPassword())) {
            throw new EditFailException();
        }
        
        memberJpaEntity.edit(dto, encoder);
    }

    @Transactional
    public void deleteMemberByAdmin(Long memberId) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        memberJpaRepository.delete(memberJpaEntity);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAllMember() {
        List<MemberJpaEntity> memberJpaEntities = memberJpaRepository.findAll();
        
        return MemberResponse.fromList(memberJpaEntities);
    }
    
    @Transactional
    public void editMemberAuth(Long myMemberId, Long memberId, MemberRoleEditDto dto) {
        MemberJpaEntity targetMemberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        MemberJpaEntity me = memberJpaRepository.findById(myMemberId).orElseThrow(MemberNotFound::new);
        
        if (me.getClub() != targetMemberJpaEntity.getClub()) {
            throw new EditFailException();
        }
        
        if (dto.getRole() == Role.ADMIN) {
            throw new EditRoleToAdminException();
        }
        
        targetMemberJpaEntity.editRole(dto);
    }
    
    @Transactional
    public void deleteMember(Long myMemberId, Long memberId) {
        MemberJpaEntity targetMemberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        MemberJpaEntity me = memberJpaRepository.findById(myMemberId).orElseThrow(MemberNotFound::new);
        
        if (me.getClub() != targetMemberJpaEntity.getClub()) {
            throw new DeleteFailException();
        }
        
        memberJpaRepository.delete(targetMemberJpaEntity);
    }
    
    @Transactional
    public void editMemberAuthByAdmin(Long id, MemberRoleEditDto dto) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(id).orElseThrow(MemberNotFound::new);

        memberJpaEntity.editRole(dto);
    }
    
    @Transactional(readOnly = true)
    public MemberStatusResponse findMyMemberDetail(Long memberId) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);

        return MemberStatusResponse.from(memberJpaEntity);
    }
}
