package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.service.AttendanceService;
import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.enums.Attendance;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	
	private static final LocalDateTime NOW = LocalDateTime.of(2024, 5, 17, 15, 0);
	private static final LocalDateTime ATTEND_TIME = NOW.minusMinutes(10);
	private static final LocalDateTime LATE_TIME = NOW.plusMinutes(10);
	
	@Test
	@DisplayName("한 예약의 참가자 조회")
	void getAttendance() {
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
				ReservationParticipate.builder().reservation(reservation).member(member1).attendance(Attendance.NOT_YET_ATTEND).build(),
				ReservationParticipate.builder().reservation(reservation).member(member2).attendance(Attendance.ATTEND).build(),
				ReservationParticipate.builder().reservation(reservation).member(member3).attendance(Attendance.LATE).build(),
				ReservationParticipate.builder().reservation(reservation).member(member4).attendance(Attendance.NO_SHOW).build()
		));
		
		//when
		List<AttendanceResponse> result = attendanceService.findAttendance(reservation.getId());
		
		//then
	    assertEquals(4, result.size());
	    assertEquals("email1", result.get(0).getMember().getEmail());
	    assertEquals(Attendance.NOT_YET_ATTEND.korName, result.get(0).getAttendance());
	    assertEquals("email2", result.get(1).getMember().getEmail());
	    assertEquals(Attendance.ATTEND.korName, result.get(1).getAttendance());
	    assertEquals("email3", result.get(2).getMember().getEmail());
	    assertEquals(Attendance.LATE.korName, result.get(2).getAttendance());
	    assertEquals("email4", result.get(3).getMember().getEmail());
	    assertEquals(Attendance.NO_SHOW.korName, result.get(3).getAttendance());
	}
	
	@Test
	@DisplayName("예약 출석 - 출석")
	void attendReservation() {
		//given
		Member member = memberRepository.save(Member.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		Reservation reservation = reservationRepository.save(Reservation.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(false)
				.build());
		reservationParticipateRepository.saveAll(List.of(
				ReservationParticipate.builder().reservation(reservation).member(member).attendance(Attendance.NOT_YET_ATTEND).build()
		));
		
		
		//when
		AttendanceResponse result = attendanceService.attendReservation(member.getId(), reservation.getId(), ATTEND_TIME);
		
		//then
		assertEquals("email1", result.getMember().getEmail());
		assertEquals(Attendance.ATTEND.korName, result.getAttendance());
	}
	
	@Test
	@DisplayName("예약 출석 - 지각")
	void attendReservation2() {
		//given
		Member member = memberRepository.save(Member.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		Reservation reservation = reservationRepository.save(Reservation.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(false)
				.build());
		reservationParticipateRepository.saveAll(List.of(
				ReservationParticipate.builder().reservation(reservation).member(member).attendance(Attendance.NOT_YET_ATTEND).build()
		));
		
		
		//when
		AttendanceResponse result = attendanceService.attendReservation(member.getId(), reservation.getId(), LATE_TIME);
		
		//then
		assertEquals("email1", result.getMember().getEmail());
		assertEquals(Attendance.LATE.korName, result.getAttendance());
	}
	
	@Test
	@DisplayName("외부 참여 가능 예약의 외부 참여")
	void attendReservationOutsider() {
		//given
		Member member = memberRepository.save(Member.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		Reservation reservation = reservationRepository.save(Reservation.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(true)
				.build());
		
		//when
		AttendanceResponse result = attendanceService.attendReservation(member.getId(), reservation.getId(), ATTEND_TIME);
		
		//then
		assertEquals("email1", result.getMember().getEmail());
		assertEquals(Attendance.ATTEND.korName, result.getAttendance());
		assertEquals(1, reservationParticipateRepository.count());
	}
	
	@Test
	@DisplayName("외부 참여 불가능 예약의 외부 참여")
	void attendReservationOutsiderFail() {
		//given
		Member member = memberRepository.save(Member.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		Reservation reservation = reservationRepository.save(Reservation.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(false)
				.build());
		
		//when
		AttendanceNotFound e = assertThrows(AttendanceNotFound.class, () -> attendanceService.attendReservation(member.getId(), reservation.getId(), ATTEND_TIME));
		
		//then
		assertEquals("404", e.statusCode());
		assertEquals("출석현황을 찾을 수 없습니다.", e.getMessage());
	}
	
	@Test
	@DisplayName("예약 출석 마감")
	void closeAttendance() {
		//given
		Member member = memberRepository.save(Member.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		Reservation reservation = reservationRepository.save(Reservation.builder()
				.creator(member)
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.build());
		reservationParticipateRepository.saveAll(List.of(
				ReservationParticipate.builder().reservation(reservation).member(member).attendance(Attendance.NOT_YET_ATTEND).build()
		));
		
		
		//when
		List<AttendanceResponse> result = attendanceService.closeAttendance(member.getId(), reservation.getId());
		
		//then
		assertEquals("email1", result.get(0).getMember().getEmail());
		assertEquals(Attendance.NO_SHOW.korName, result.get(0).getAttendance());
	}

}