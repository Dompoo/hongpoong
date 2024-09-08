package Dompoo.Hongpoong.api.controller.reservation;

import Dompoo.Hongpoong.api.dto.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.ReservationResponse;
import Dompoo.Hongpoong.common.security.UserClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "3. 예약")
public interface ReservationApi {
	
	@Operation(summary = "예약 등록")
	void createReservation(UserClaims claims, ReservationCreateRequest request);
	
	@Operation(summary = "특정 연도/달의 예약 전체 조회")
	List<ReservationResponse> findAllReservationOfYearAndMonth(Integer year, Integer month);
	
	@Operation(summary = "특정 날짜의 예약 전체 조회")
	List<ReservationResponse> findAllReservationOfDate(LocalDate date);
	
	@Operation(summary = "나의 오늘자 예약 조회")
	List<ReservationResponse> findAllTodoReservationOfToday(UserClaims claims);
	
	@Operation(summary = "예약 상세 조회")
	ReservationResponse findReservationDetail(Long reservationId);
	
	@Operation(summary = "특정 예약에 참석하지 않은 회원 조회")
	List<MemberResponse> findAllNotAttendedMember(Long reservationId);
	
	@Operation(summary = "예약 수정")
	ReservationResponse editReservation(UserClaims claims, Long id, ReservationEditRequest request);
	
	@Operation(summary = "예약 삭제")
	void deleteReservation(UserClaims claims, Long id);
	
	@Operation(summary = "예약 수정")
	void edit(Long id, ReservationEditRequest request);
}
