package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.UserPrincipal;
import Dompoo.Hongpoong.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.request.reservation.ReservationShiftRequest;
import Dompoo.Hongpoong.response.MenuResponse;
import Dompoo.Hongpoong.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    public final ReservationService service;

    @GetMapping("")
    public List<MenuResponse> reservationMenu() {
        return service.getList();
    }

    @PostMapping("")
    public MenuResponse addReservation(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid ReservationCreateRequest request) {
        return service.addReservation(principal.getMemberId(), request);
    }

    @GetMapping("/{id}")
    public MenuResponse reservationDetail(@PathVariable Long id) {
        return service.findReservation(id);
    }

    @PostMapping("/{id}")
    public void shiftReservation(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, @RequestBody @Valid ReservationShiftRequest request) {
        service.shiftReservation(principal.getMemberId(), id, request);
    }

    @PatchMapping("/{id}")
    public void editReservation(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        service.editReservation(principal.getMemberId(), id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        service.deleteReservation(principal.getMemberId(), id);
    }
}
