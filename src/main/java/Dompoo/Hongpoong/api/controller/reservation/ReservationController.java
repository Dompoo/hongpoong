package Dompoo.Hongpoong.api.controller.reservation;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.api.service.AttendanceService;
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
    private final AttendanceService attendanceService;
    
    @Secured
    @PostMapping
    public ReservationDetailResponse createReservation(@LoginUser UserClaims claims, @RequestBody @Valid ReservationCreateRequest request) {
        return reservationService.createReservation(claims.getId(), request);
    }
    
    @Secured
    @GetMapping("/year-month")
    public List<ReservationResponse> findAllReservationOfYearAndMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        return reservationService.findAllReservationOfYearAndMonth(year, month);
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
    public ReservationDetailResponse findReservationDetail(@PathVariable Long reservationId) {
        return reservationService.findReservationDetail(reservationId);
    }
    
    @Secured
    @GetMapping("/{reservationId}/attendance")
    public List<AttendanceResponse> findAttendance(@PathVariable Long reservationId) {
        return attendanceService.findAttendance(reservationId);
    }
    
    @Secured
    @PatchMapping("/{reservationId}/extend")
    public void extendReservationTime(@LoginUser UserClaims claims, @PathVariable Long reservationId) {
        reservationService.extendReservationTime(claims.getId(), reservationId, LocalTime.now());
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
