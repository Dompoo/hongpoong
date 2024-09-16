package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.service.AttendanceService;
import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.enums.Role;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
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
class AttendanceJpaEntityStatusServiceTest {
	
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private AttendanceJpaRepository attendanceJpaRepository;
	@Autowired
	private MemberJpaRepository memberJpaRepository;
	@Autowired
	private ReservationJpaRepository reservationJpaRepository;
	
	@AfterEach
	void tearDown() {
		attendanceJpaRepository.deleteAllInBatch();
		reservationJpaRepository.deleteAllInBatch();
		memberJpaRepository.deleteAllInBatch();
	}
	
	private static final LocalDateTime NOW = LocalDateTime.of(2024, 5, 17, 15, 0);
	private static final LocalDateTime ATTEND_TIME = NOW.minusMinutes(10);
	private static final LocalDateTime LATE_TIME = NOW.plusMinutes(10);
	
	@Test
	@DisplayName("한 예약의 참가자 조회")
	void getAttendance() {
	    //given
		MemberJpaEntity memberJpaEntity1 = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email1")
				.name("name1")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		MemberJpaEntity memberJpaEntity2 = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email2")
				.name("name2")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		MemberJpaEntity memberJpaEntity3 = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email3")
				.name("name3")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		MemberJpaEntity memberJpaEntity4 = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email4")
				.name("name4")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
				.creator(memberJpaEntity1)
				.build());
		attendanceJpaRepository.saveAll(List.of(
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity1).attendanceStatus(AttendanceStatus.NOT_YET_ATTEND).build(),
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity2).attendanceStatus(AttendanceStatus.ATTEND).build(),
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity3).attendanceStatus(AttendanceStatus.LATE).build(),
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity4).attendanceStatus(AttendanceStatus.NO_SHOW).build()
		));
		
		//when
		List<AttendanceResponse> result = attendanceService.findAttendance(reservationJpaEntity.getId());
		
		//then
	    assertEquals(4, result.size());
	    assertEquals("email1", result.get(0).getMember().getEmail());
	    assertEquals(AttendanceStatus.NOT_YET_ATTEND.korName, result.get(0).getAttendance());
	    assertEquals("email2", result.get(1).getMember().getEmail());
	    assertEquals(AttendanceStatus.ATTEND.korName, result.get(1).getAttendance());
	    assertEquals("email3", result.get(2).getMember().getEmail());
	    assertEquals(AttendanceStatus.LATE.korName, result.get(2).getAttendance());
	    assertEquals("email4", result.get(3).getMember().getEmail());
	    assertEquals(AttendanceStatus.NO_SHOW.korName, result.get(3).getAttendance());
	}
	
	@Test
	@DisplayName("예약 출석 - 출석")
	void attendReservation() {
		//given
		MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email1")
				.name("name1")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(false)
				.build());
		attendanceJpaRepository.saveAll(List.of(
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity).attendanceStatus(AttendanceStatus.NOT_YET_ATTEND).build()
		));
		
		
		//when
		AttendanceResponse result = attendanceService.attendReservation(memberJpaEntity.getId(), reservationJpaEntity.getId(), ATTEND_TIME);
		
		//then
		assertEquals("email1", result.getMember().getEmail());
		assertEquals(AttendanceStatus.ATTEND.korName, result.getAttendance());
	}
	
	@Test
	@DisplayName("예약 출석 - 지각")
	void attendReservation2() {
		//given
		MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email1")
				.name("name1")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(false)
				.build());
		attendanceJpaRepository.saveAll(List.of(
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity).attendanceStatus(AttendanceStatus.NOT_YET_ATTEND).build()
		));
		
		
		//when
		AttendanceResponse result = attendanceService.attendReservation(memberJpaEntity.getId(), reservationJpaEntity.getId(), LATE_TIME);
		
		//then
		assertEquals("email1", result.getMember().getEmail());
		assertEquals(AttendanceStatus.LATE.korName, result.getAttendance());
	}
	
	@Test
	@DisplayName("외부 참여 가능 예약의 외부 참여")
	void attendReservationOutsider() {
		//given
		MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email1")
				.name("name1")
				.role(Role.LEADER)
				.club(Club.SANTLE)
				.build());
		ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(true)
				.build());
		
		//when
		AttendanceResponse result = attendanceService.attendReservation(memberJpaEntity.getId(), reservationJpaEntity.getId(), ATTEND_TIME);
		
		//then
		assertEquals("email1", result.getMember().getEmail());
		assertEquals(AttendanceStatus.ATTEND.korName, result.getAttendance());
		assertEquals(1, attendanceJpaRepository.count());
	}
	
	@Test
	@DisplayName("외부 참여 불가능 예약의 외부 참여")
	void attendReservationOutsiderFail() {
		//given
		MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.build());
		ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.participationAvailable(false)
				.build());
		
		//when
		AttendanceNotFound e = assertThrows(AttendanceNotFound.class, () -> attendanceService.attendReservation(memberJpaEntity.getId(), reservationJpaEntity.getId(), ATTEND_TIME));
		
		//then
		assertEquals("404", e.statusCode());
		assertEquals("출석현황을 찾을 수 없습니다.", e.getMessage());
	}
	
	@Test
	@DisplayName("예약 출석 마감")
	void closeAttendance() {
		//given
		MemberJpaEntity memberJpaEntity = memberJpaRepository.save(MemberJpaEntity.builder()
				.email("email1")
				.name("name1")
				.club(Club.SANTLE)
				.role(Role.LEADER)
				.build());
		ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.save(ReservationJpaEntity.builder()
				.creator(memberJpaEntity)
				.date(NOW.toLocalDate())
				.endTime(ReservationTime.from(NOW.toLocalTime()))
				.build());
		attendanceJpaRepository.saveAll(List.of(
				AttendanceJpaEntity.builder().reservationJpaEntity(reservationJpaEntity).memberJpaEntity(memberJpaEntity).attendanceStatus(AttendanceStatus.NOT_YET_ATTEND).build()
		));
		
		
		//when
		List<AttendanceResponse> result = attendanceService.closeAttendance(memberJpaEntity.getId(), reservationJpaEntity.getId());
		
		//then
		assertEquals("email1", result.get(0).getMember().getEmail());
		assertEquals(AttendanceStatus.NO_SHOW.korName, result.get(0).getAttendance());
	}

}