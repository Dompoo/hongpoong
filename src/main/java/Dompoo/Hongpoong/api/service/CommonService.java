package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditDto;
import Dompoo.Hongpoong.api.dto.common.response.SettingResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional(readOnly = true)
    public SettingResponse findMySetting(Long memberId) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        return SettingResponse.from(memberJpaEntity);
    }

    @Transactional
    public void editSetting(Long memberId, SettingEditDto dto) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        memberJpaEntity.editSetting(dto);
    }
}
