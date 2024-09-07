package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.response.member.MemberResponse;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
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
public class ReservationController {

    private final ReservationService reservationService;
    private final AttendanceService attendanceService;
    
    @Secured
    @PostMapping
    public void addReservation(@LoginUser UserClaims claims, @RequestBody @Valid ReservationCreateRequest request) {
        reservationService.addReservation(claims.getId(), request);
    }
    
    @Secured
    @GetMapping("/year-month")
    public List<ReservationResponse> getAllReservationOfYearAndMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        return reservationService.getAllReservationOfYearAndMonth(year, month);
    }
    
    @Secured
    @GetMapping("/day")
    public List<ReservationResponse> getAllReservationOfDate(@RequestParam("date") LocalDate date) {
        return reservationService.getAllReservationOfDate(date);
    }
    
    @Secured
    @GetMapping("/todo")
    public List<ReservationResponse> getAllTodoReservationOfToday(@LoginUser UserClaims claims) {
        return reservationService.getAllTodoReservationOfToday(claims.getId(), LocalDate.now());
    }
    
    @Secured
    @GetMapping("/{reservationId}")
    public ReservationResponse getReservationDetail(@PathVariable Long reservationId) {
        return reservationService.getReservationDetail(reservationId);
    }
    
    @Secured
    @GetMapping("/{reservationId}/attendance")
    public List<MemberResponse> getAllNotAttendedMember(@PathVariable Long reservationId) {
        return attendanceService.getAllNotAttendedMember(reservationId);
    }
    
    @Secured
    @PatchMapping("/{id}")
    public ReservationResponse editReservation(@LoginUser UserClaims claims, @PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        return reservationService.editReservation(claims.getId(), id, request.toDto(), LocalDateTime.now());
    }
    
    @Secured
    @DeleteMapping("/{id}")
    public void deleteReservation(@LoginUser UserClaims claims, @PathVariable Long id) {
        reservationService.deleteReservation(claims.getId(), id);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PatchMapping("/manage/{id}")
    public void edit(@PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        reservationService.edit(id, request.toDto(), LocalDateTime.now());
    }
}
