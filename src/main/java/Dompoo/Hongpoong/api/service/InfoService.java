package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditDto;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.InfoNotFound;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Info;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InfoJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {
    
    private final InfoJpaRepository infoJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public void createInfo(Long memberId, InfoCreateRequest request, LocalDateTime now) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        Info info = request.toInfo(member, now);
        
        infoJpaRepository.save(info);
    }

    @Transactional(readOnly = true)
    public List<InfoResponse> findAllInfo() {
        return infoJpaRepository.findAll().stream()
                .map(InfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public InfoDetailResponse findInfoDetail(Long infoId) {
        Info info = infoJpaRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        return InfoDetailResponse.from(info);
    }
    
    @Transactional
    public void editInfo(Long memberId, Long infoId, InfoEditDto dto) {
        Info info = infoJpaRepository.findByIdFetchJoinMember(infoId).orElseThrow(InfoNotFound::new);
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!info.getMember().getClub().equals(member.getClub())) {
            throw new EditFailException();
        }
        
        info.edit(dto);
    }
    
    @Transactional
    public void deleteInfo(Long memberId, Long infoId) {
        Info info = infoJpaRepository.findByIdFetchJoinMember(infoId).orElseThrow(InfoNotFound::new);
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!info.getMember().getClub().equals(member.getClub())) {
            throw new DeleteFailException();
        }
        
        infoJpaRepository.delete(info);
    }
    
    @Transactional
    public void editInfoByAdmin(Long infoId, InfoEditDto dto) {
        Info info = infoJpaRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);
        
        info.edit(dto);
    }
    
    @Transactional
    public void deleteInfoByAdmin(Long infoId) {
        Info info = infoJpaRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        infoJpaRepository.delete(info);
    }
}
