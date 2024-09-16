package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.config.MyWebMvcTest;
import Dompoo.Hongpoong.domain.enums.AttendanceStatus;
import Dompoo.Hongpoong.domain.enums.Club;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AttendanceStatusControllerTest extends MyWebMvcTest {
	
	private static final Long RESERVATION_ID = 1L;
	private static final String NAME = "창근";
	private static final String NICKNAME = "불꽃남자";
	private static final String EMAIL = "dompoo@gmail.com";
	private static final Integer ENROLLMENT_NUMBER = 19;
	private static final Club CLUB = Club.SANTLE;
	private static final AttendanceStatus ATTENDANCE_STATUS = AttendanceStatus.NOT_YET_ATTEND;
	private static final AttendanceStatus ATTENDANCE_STATUS_2 = AttendanceStatus.ATTEND;
	
	@Test
	@DisplayName("해당 예약의 참석여부 조회")
	void findAllMemberNotAttended() throws Exception {
		//given
		when(attendanceService.findAttendance(any())).thenReturn(List.of(
				AttendanceResponse.builder().member(buildMemberResponse()).attendance(ATTENDANCE_STATUS.korName).build(),
				AttendanceResponse.builder().member(buildMemberResponse()).attendance(ATTENDANCE_STATUS_2.korName).build()
		));
		
		//expected
		mockMvc.perform(get("/attendance/{id}", RESERVATION_ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].member.name").value(NAME))
				.andExpect(jsonPath("$[0].member.email").value(EMAIL))
				.andExpect(jsonPath("$[0].attendance").value(ATTENDANCE_STATUS.korName))
				.andExpect(jsonPath("$[1].member.name").value(NAME))
				.andExpect(jsonPath("$[1].member.email").value(EMAIL))
				.andExpect(jsonPath("$[1].attendance").value(ATTENDANCE_STATUS_2.korName))
				.andDo(print());
	}
	
	@Test
	@DisplayName("예약 참석")
	void attendReservation() throws Exception {
		//given
		when(attendanceService.attendReservation(any(), any(), any())).thenReturn(
				AttendanceResponse.builder()
						.member(buildMemberResponse())
						.attendance(ATTENDANCE_STATUS.korName)
						.build());
		
		//expected
		mockMvc.perform(post("/attendance/{id}/attend", RESERVATION_ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.member.name").value(NAME))
				.andExpect(jsonPath("$.member.email").value(EMAIL))
				.andExpect(jsonPath("$.attendance").value(ATTENDANCE_STATUS.korName))
				.andDo(print());
	}
	
	@Test
	@DisplayName("해당 예약 참석끝내기")
	void closeAttendance() throws Exception {
		//given
		when(attendanceService.closeAttendance(any(), any())).thenReturn(List.of(
				AttendanceResponse.builder().member(buildMemberResponse()).attendance(ATTENDANCE_STATUS.korName).build(),
				AttendanceResponse.builder().member(buildMemberResponse()).attendance(ATTENDANCE_STATUS_2.korName).build()
		));
		
		//expected
		mockMvc.perform(post("/attendance/{id}/close", RESERVATION_ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].member.name").value(NAME))
				.andExpect(jsonPath("$[0].member.email").value(EMAIL))
				.andExpect(jsonPath("$[0].attendance").value(ATTENDANCE_STATUS.korName))
				.andExpect(jsonPath("$[1].member.name").value(NAME))
				.andExpect(jsonPath("$[1].member.email").value(EMAIL))
				.andExpect(jsonPath("$[1].attendance").value(ATTENDANCE_STATUS_2.korName))
				.andDo(print());
	}
	
	private MemberResponse buildMemberResponse() {
		return MemberResponse.builder()
				.name(NAME)
				.nickname(NICKNAME)
				.email(EMAIL)
				.enrollmentNumber(ENROLLMENT_NUMBER)
				.club(CLUB.korName)
				.build();
	}
	
}