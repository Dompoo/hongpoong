package Dompoo.Hongpoong.api.controller.attendance;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "5. 출석")
public interface AttendanceApi {
	
	@Operation(summary = "특정 예약 출석여부 전체 조회")
	List<AttendanceResponse> findAttendance(
			@Parameter(description = "예약 id") Long reservationId
	);
	
	@Operation(summary = "출석하기")
	AttendanceResponse attendReservation(
			@Schema(hidden = true) UserClaims userClaims,
			@Parameter(description = "예약 id") Long reservationId
	);
	
	@Operation(summary = "특정 예약 출석 그만 받기")
	List<AttendanceResponse> clostAttendance(
			@Schema(hidden = true) UserClaims userClaims,
			@Parameter(description = "예약 id") Long reservationId
	);
}
