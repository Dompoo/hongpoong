package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.api.service.ReservationService;
import Dompoo.Hongpoong.common.security.LoginUser;
import Dompoo.Hongpoong.common.security.UserClaims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    public final ReservationService service;

    @PostMapping("/search")
    public List<ReservationResponse> reservationMenu(@RequestBody @Valid ReservationSearchRequest request) {
        return service.getList(request);
    }

    @PostMapping
    public void addReservation(@LoginUser UserClaims claims, @RequestBody @Valid ReservationCreateRequest request) {
        service.addReservation(claims.getId(), request);
    }

    @GetMapping("/{id}")
    public ReservationResponse reservationDetail(@PathVariable Long id) {
        return service.findReservation(id);
    }

    @PatchMapping("/{id}")
    public ReservationResponse editReservation(@LoginUser UserClaims claims, @PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        return service.editReservation(claims.getId(), id, request.toDto(), LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@LoginUser UserClaims claims, @PathVariable Long id) {
        service.deleteReservation(claims.getId(), id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/manage/{id}")
    public void edit(@PathVariable Long id, @RequestBody @Valid ReservationEditRequest request) {
        service.edit(id, request.toDto(), LocalDateTime.now());
    }
}
