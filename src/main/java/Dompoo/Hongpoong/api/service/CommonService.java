package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.common.SettingResponse;
import Dompoo.Hongpoong.api.dto.common.SettingSaveDto;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public SettingResponse getSetting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        return SettingResponse.from(member);
    }

    @Transactional
    public void saveSetting(Long memberId, SettingSaveDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        member.editSetting(dto);
    }
}
