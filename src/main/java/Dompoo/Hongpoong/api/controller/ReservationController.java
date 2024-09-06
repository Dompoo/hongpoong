package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
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

    public final ReservationService service;

    @Secured
    @GetMapping("/year-month")
    public List<ReservationResponse> getAllReservationOfYearAndMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        return service.getAllReservationOfYearAndMonth(year, month);
    }
    
    @Secured
    @GetMapping("/day")
    public List<ReservationResponse> getAllReservationOfDate(@RequestParam("date") LocalDate date) {
        return service.getAllReservationOfDate(date);
    }
    
    @Secured
    @GetMapping("/todo")
    public List<ReservationResponse> getAllTodoReservationOfToday(@LoginUser UserClaims claims) {
        return service.getAllTodoReservationOfToday(claims.getId(), LocalDate.now());
    }
    
    @Secured
    @GetMapping("/{id}")
    public ReservationResponse getReservationDetail(@PathVariable Long id) {
        return service.findReservation(id);
    }
    
    @Secured
    @PostMapping
    public void addReservation(@LoginUser UserClaims claims, @RequestBody @Valid ReservationCreateRequest request) {
        service.addReservation(claims.getId(), request);
    }
    
    @Secured
    @PatchMapping("/{id}")
    public ReservationResponse editReservation(@LoginUser UserClaims claims, @PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        return service.editReservation(claims.getId(), id, request.toDto(), LocalDateTime.now());
    }
    
    @Secured
    @DeleteMapping("/{id}")
    public void deleteReservation(@LoginUser UserClaims claims, @PathVariable Long id) {
        service.deleteReservation(claims.getId(), id);
    }

    @Secured(SecurePolicy.ADMIN_ONLY)
    @PatchMapping("/manage/{id}")
    public void edit(@PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        service.edit(id, request.toDto(), LocalDateTime.now());
    }
}
