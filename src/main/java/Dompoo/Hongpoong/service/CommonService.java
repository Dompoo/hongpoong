package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.common.SettingSaveDto;
import Dompoo.Hongpoong.api.dto.response.common.SettingResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.Setting;
import Dompoo.Hongpoong.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonService {

    private final MemberRepository memberRepository;

    public SettingResponse getSetting(Long memberId) {
        Setting setting = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new)
                .getSetting();

        return SettingResponse.from(setting);
    }

    public void saveSetting(Long memberId, SettingSaveDto dto) {
        Setting setting = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new)
                .getSetting();
        
        setting.edit(dto);
    }
}
