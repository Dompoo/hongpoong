package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditDto;
import Dompoo.Hongpoong.api.dto.common.response.SettingResponse;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public SettingResponse findMySetting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        return SettingResponse.from(member);
    }

    @Transactional
    public void editSetting(Long memberId, SettingEditDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        Member editedMember = member.withEditedSetting(dto.getPush());
        memberRepository.save(editedMember);
    }
}
