package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    public final ReservationService service;

    @GetMapping("")
    public List<ReservationResponse> reservationMenu() {
        return service.getList();
    }

    @PostMapping("")
    public void addReservation(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid ReservationCreateRequest request) {
        service.addReservation(principal.getMemberId(), request);
    }

    @GetMapping("/{id}")
    public ReservationResponse reservationDetail(@PathVariable Long id) {
        return service.findReservation(id);
    }

    @PatchMapping("/{id}")
    public void editReservation(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        service.editReservation(principal.getMemberId(), id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        service.deleteReservation(principal.getMemberId(), id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/manage/{id}")
    public void edit(@PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        service.edit(id, request);
    }
}
