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
import Dompoo.Hongpoong.domain.repository.InfoRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoService {
    
    private final InfoRepository infoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createInfo(Long memberId, InfoCreateRequest request, LocalDateTime now) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        Info info = request.toInfo(member, now);
        
        infoRepository.save(info);
    }

    @Transactional(readOnly = true)
    public List<InfoResponse> findAllInfo() {
        return infoRepository.findAll().stream()
                .map(InfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public InfoDetailResponse findInfoDetail(Long infoId) {
        Info info = infoRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        return InfoDetailResponse.from(info);
    }
    
    @Transactional
    public void editInfo(Long memberId, Long infoId, InfoEditDto dto) {
        Info info = infoRepository.findByIdFetchJoinMember(infoId).orElseThrow(InfoNotFound::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!info.getMember().getClub().equals(member.getClub())) {
            throw new EditFailException();
        }
        
        info.edit(dto);
    }
    
    @Transactional
    public void deleteInfo(Long memberId, Long infoId) {
        Info info = infoRepository.findByIdFetchJoinMember(infoId).orElseThrow(InfoNotFound::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        if (!info.getMember().getClub().equals(member.getClub())) {
            throw new DeleteFailException();
        }
        
        infoRepository.delete(info);
    }
    
    @Transactional
    public void editInfoByAdmin(Long infoId, InfoEditDto dto) {
        Info info = infoRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);
        
        info.edit(dto);
    }
    
    @Transactional
    public void deleteInfoByAdmin(Long infoId) {
        Info info = infoRepository.findById(infoId)
                .orElseThrow(InfoNotFound::new);

        infoRepository.delete(info);
    }
}
