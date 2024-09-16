package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditDto;
import Dompoo.Hongpoong.api.dto.common.response.SettingResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
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
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        return SettingResponse.from(member);
    }

    @Transactional
    public void editSetting(Long memberId, SettingEditDto dto) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        member.editSetting(dto);
    }
}
