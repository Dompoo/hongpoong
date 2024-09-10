package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.service.AttendanceService;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class AttendanceServiceTest {
	
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private ReservationParticipateRepository reservationParticipateRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	
	@AfterEach
	void tearDown() {
		reservationParticipateRepository.deleteAllInBatch();
		reservationRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}
	
	@Test
	@DisplayName("한 예약의 참가자 조회")
	void test() {
	    //given
		Member member1 = memberRepository.save(Member.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		Member member2 = memberRepository.save(Member.builder()
				.email("email2")
				.name("name2")
				.club(Club.SANTLE)
				.build());
		Member member3 = memberRepository.save(Member.builder()
				.email("email3")
				.name("name3")
				.club(Club.SANTLE)
				.build());
		Member member4 = memberRepository.save(Member.builder()
				.email("email4")
				.name("name4")
				.club(Club.SANTLE)
				.build());
		Reservation reservation = reservationRepository.save(Reservation.builder()
				.creator(member1)
				.build());
		reservationParticipateRepository.saveAll(List.of(
				ReservationParticipate.builder().reservation(reservation).member(member1).build(),
				ReservationParticipate.builder().reservation(reservation).member(member2).build(),
				ReservationParticipate.builder().reservation(reservation).member(member3).build(),
				ReservationParticipate.builder().reservation(reservation).member(member4).build()
		));
		
		//when
		List<AttendanceResponse> result = attendanceService.findAttendance(reservation.getId());
		
		//then
	    Assertions.assertEquals(4, result.size());
	    Assertions.assertEquals("email1", result.get(0).getEmail());
	    Assertions.assertEquals("email2", result.get(1).getEmail());
	    Assertions.assertEquals("email3", result.get(2).getEmail());
	    Assertions.assertEquals("email4", result.get(3).getEmail());
	}

}