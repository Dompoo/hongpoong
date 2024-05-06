package Dompoo.Hongpoong;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.member.MemberRoleEditRequest;
import Dompoo.Hongpoong.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class AdminInit {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void initAdmin() {
        Member member = memberRepository.save(Member.builder()
                .email("admin")
                .username("admin")
                .password(encoder.encode("1234"))
                .club(Member.Club.SANTLE)
                .build());

        memberService.editRole(member.getId(), MemberRoleEditRequest.builder().isAdmin(true).build());

        log.info("[어드민 계정 생성, id:{}]", member.getId());
        log.warn("[어드민 계정의 비밀번호는 1234입니다. 꼭 변경해주세요.]");
    }
}
