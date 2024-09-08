package Dompoo.Hongpoong.api.controller.reservation;

import Dompoo.Hongpoong.api.dto.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.reservation.ReservationResponse;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController implements ReservationApi {

    private final ReservationService reservationService;
    private final AttendanceService attendanceService;
    
    @Secured
    @PostMapping
    public void createReservation(@LoginUser UserClaims claims, @RequestBody @Valid ReservationCreateRequest request) {
        reservationService.createReservation(claims.getId(), request);
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
    public ReservationResponse findReservationDetail(@PathVariable Long reservationId) {
        return reservationService.findReservationDetail(reservationId);
    }
    
    @Secured
    @GetMapping("/{reservationId}/attendance")
    public List<MemberResponse> findAllNotAttendedMember(@PathVariable Long reservationId) {
        return attendanceService.findAllNotAttendedMember(reservationId);
    }
    
    @Secured
    @PatchMapping("/{reservationId}")
    public ReservationResponse editReservation(@LoginUser UserClaims claims, @PathVariable Long reservationId, @RequestBody @Valid ReservationEditRequest request) {
        return reservationService.editReservation(claims.getId(), reservationId, request.toDto(), LocalDateTime.now());
    }
    
    @Secured
    @DeleteMapping("/{reservationId}")
    public void deleteReservation(@LoginUser UserClaims claims, @PathVariable Long reservationId) {
        reservationService.deleteReservation(claims.getId(), reservationId);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PatchMapping("/manage/{reservationId}")
    public void edit(@PathVariable Long reservationId, @RequestBody @Valid ReservationEditRequest request) {
        reservationService.edit(reservationId, request.toDto(), LocalDateTime.now());
    }
}
