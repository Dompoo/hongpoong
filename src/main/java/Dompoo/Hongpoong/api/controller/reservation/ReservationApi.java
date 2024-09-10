package Dompoo.Hongpoong.api.controller.reservation;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "3. 예약")
public interface ReservationApi {
	
	@Operation(summary = "예약 등록")
	ReservationDetailResponse createReservation(
			@Schema(hidden = true) UserClaims claims,
			@RequestBody ReservationCreateRequest request
	);
	
	@Operation(summary = "특정 연도/달의 예약 전체 조회")
	List<ReservationResponse> findAllReservationOfYearAndMonth(
			@Parameter(description = "연도") Integer year,
			@Parameter(description = "달") Integer month
	);
	
	@Operation(summary = "특정 날짜의 예약 전체 조회")
	List<ReservationResponse> findAllReservationOfDate(
			@Parameter(description = "날짜") LocalDate date
	);
	
	@Operation(summary = "나의 오늘자 예약 조회")
	List<ReservationResponse> findAllTodoReservationOfToday(
			@Schema(hidden = true) UserClaims claims
	);
	
	@Operation(summary = "예약 상세 조회")
	ReservationDetailResponse findReservationDetail(
			@Parameter(description = "예약 id") Long reservationId
	);
	
	@Operation(summary = "특정 예약의 회원 참석여부 전체 조회")
	List<AttendanceResponse> findAttendance(
			@Parameter(description = "예약 id") Long reservationId
	);
	
	@Operation(summary = "내가 등록한 예약 수정")
	ReservationDetailResponse editReservation(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "예약 id") Long reservationId,
			@RequestBody ReservationEditRequest request
	);
	
	@Operation(summary = "내가 등록한 예약 삭제")
	void deleteReservation(
			@Schema(hidden = true) UserClaims claims,
			@Parameter(description = "예약 id") Long reservationId
	);
	
	@Operation(summary = "[관리자] 예약 수정")
	void edit(
			@Parameter(description = "예약 id") Long reservationId,
			@RequestBody ReservationEditRequest request
	);
}
