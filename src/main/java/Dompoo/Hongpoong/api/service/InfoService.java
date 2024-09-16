package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.info.request.InfoCreateRequest;
import Dompoo.Hongpoong.api.dto.info.request.InfoEditDto;
import Dompoo.Hongpoong.api.dto.info.response.InfoDetailResponse;
import Dompoo.Hongpoong.api.dto.info.response.InfoResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.InfoNotFound;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.InfoJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        InfoJpaEntity infoJpaEntity = request.toInfo(memberJpaEntity, now);
        
        infoJpaRepository.save(infoJpaEntity);
    }

    @Transactional(readOnly = true)
    public List<InfoResponse> findAllInfo() {
        return infoJpaRepository.findAll().stream()
                .map(InfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public InfoDetailResponse findInfoDetail(Long infoId) {
        InfoJpaEntity infoJpaEntity = infoJpaRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        return InfoDetailResponse.from(infoJpaEntity);
    }
    
    @Transactional
    public void editInfo(Long memberId, Long infoId, InfoEditDto dto) {
        InfoJpaEntity infoJpaEntity = infoJpaRepository.findByIdFetchJoinMember(infoId).orElseThrow(InfoNotFound::new);
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!infoJpaEntity.getMemberJpaEntity().getClub().equals(memberJpaEntity.getClub())) {
            throw new EditFailException();
        }
        
        infoJpaEntity.edit(dto);
    }
    
    @Transactional
    public void deleteInfo(Long memberId, Long infoId) {
        InfoJpaEntity infoJpaEntity = infoJpaRepository.findByIdFetchJoinMember(infoId).orElseThrow(InfoNotFound::new);
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!infoJpaEntity.getMemberJpaEntity().getClub().equals(memberJpaEntity.getClub())) {
            throw new DeleteFailException();
        }
        
        infoJpaRepository.delete(infoJpaEntity);
    }
    
    @Transactional
    public void editInfoByAdmin(Long infoId, InfoEditDto dto) {
        InfoJpaEntity infoJpaEntity = infoJpaRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);
        
        infoJpaEntity.edit(dto);
    }
    
    @Transactional
    public void deleteInfoByAdmin(Long infoId) {
        InfoJpaEntity infoJpaEntity = infoJpaRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        infoJpaRepository.delete(infoJpaEntity);
    }
}
