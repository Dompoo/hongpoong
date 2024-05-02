package Dompoo.Hongpoong;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class HongpoongApplication {

	private final MemberRepository memberRepository;

	public static void main(String[] args) {
		SpringApplication.run(HongpoongApplication.class, args);
	}

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
