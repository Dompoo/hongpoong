package Dompoo.Hongpoong.api.controller.reservation;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationBatchCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponseWithInstrument;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationEndResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.api.service.ReservationService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController implements ReservationApi {

    private final ReservationService reservationService;
    
    @Secured
    @PostMapping
    public ReservationDetailResponse createReservation(@LoginUser UserClaims claims, @RequestBody @Valid ReservationCreateRequest request) {
        return reservationService.createReservation(claims.getId(), request, LocalDateTime.now());
    }
    
    @Secured
    @GetMapping("/year-month")
    public List<ReservationResponse> findAllReservationOfYearAndMonth(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month
    ) {
        return reservationService.findAllReservationOfYearAndMonth(year, month);
    }
    
    @Override
    public List<ReservationResponse> findAllReservationOfYearAndMonthAndMemberId(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam("memberId") Long memberId
    ) {
        return reservationService.findAllReservationOfYearAndMonthAndMemberId(year, month, memberId);
    }
    
    @Secured
    @GetMapping("/day")
    public List<ReservationResponse> findAllReservationOfDate(@RequestParam("date") LocalDate date) {
        return reservationService.findAllReservationOfDate(date);
    }
    
    @Secured
    @GetMapping("/todo")
    public List<ReservationResponse> findAllTodoReservationOfToday(@LoginUser UserClaims claims) {
        return reservationService.findAllTodoReservationOfToday(claims.getId(), LocalDate.now());
    }
    
    @Secured
    @GetMapping("/{reservationId}")
    public ReservationDetailResponseWithInstrument findReservationDetail(@PathVariable Long reservationId) {
        return reservationService.findReservationDetail(reservationId);
    }
    
    @Secured
    @PatchMapping("/{reservationId}/extend")
    public void extendReservationTime(@LoginUser UserClaims claims, @PathVariable Long reservationId) {
        reservationService.extendReservationTime(claims.getId(), reservationId, LocalTime.now());
    }
    
    @Secured
    @PostMapping("/{reservationId}/end")
    public void endReservation(@LoginUser UserClaims claims, @PathVariable Long reservationId, @RequestBody ReservationEndRequest request) {
        reservationService.endReservation(claims.getId(), reservationId, request);
    }
    
    @Secured
    @PatchMapping("/{reservationId}")
    public ReservationDetailResponse editReservation(@LoginUser UserClaims claims, @PathVariable Long reservationId, @RequestBody @Valid ReservationEditRequest request) {
        return reservationService.editReservation(claims.getId(), reservationId, request.toDto(), LocalDateTime.now());
    }
    
    @Secured
    @DeleteMapping("/{reservationId}")
    public void deleteReservation(@LoginUser UserClaims claims, @PathVariable Long reservationId) {
        reservationService.deleteReservation(claims.getId(), reservationId);
    }
    
    @Secured(SecurePolicy.ADMIN)
    @GetMapping("/manage/{reservationId}")
    public ReservationEndResponse findReservationEndDetail(@PathVariable Long reservationId) {
        return reservationService.findReservationEndDetail(reservationId);
    }
    
    @Secured(SecurePolicy.ADMIN)
    @PostMapping("/manage")
    public void addReservationInBatchByAdmin(@RequestBody @Valid ReservationBatchCreateRequest request) {
        reservationService.createReservationInBatch(request, LocalDateTime.now());
    }
    
    @Secured(SecurePolicy.ADMIN)
    @PatchMapping("/manage/{reservationId}")
    public void editReservationByAdmin(@PathVariable Long reservationId, @RequestBody @Valid ReservationEditRequest request) {
        reservationService.editReservationByAdmin(reservationId, request.toDto(), LocalDateTime.now());
    }
    
    @Secured(SecurePolicy.ADMIN)
    @DeleteMapping("/manage/{reservationId}")
    public void deleteReservationByAdmin(@PathVariable Long reservationId) {
        reservationService.deleteReservationByAdmin(reservationId);
    }
}
