package Dompoo.Hongpoong;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class InitAdmin {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member admin = Member.builder()
                .email("admin@gmail.com")
                .username("의장")
                .password("qwer")
                .club(Member.Club.SANTLE)
                .build();

        admin.setRole(Member.Role.ROLE_ADMIN);
        memberRepository.save(admin);
    }
}
