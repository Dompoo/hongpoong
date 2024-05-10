package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.response.rental.RentalResponse;
import Dompoo.Hongpoong.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rental")
public class RentalController {

    private final RentalService service;

//    @GetMapping("")
//    public List<RentalResponse> rentalMenu(@AuthenticationPrincipal UserPrincipal principal) {
//        return service.getList(principal.getMemberId());
//    }

    @PostMapping("")
    public void addRental(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid RentalCreateRequest request) {
        service.addRental(principal.getMemberId(), request);
    }

    @GetMapping("/{id}")
    public RentalResponse rentalDetail(@PathVariable Long id) {
        return service.getDetail(id);
    }

//    @PutMapping("/{id}")
//    public void editRental(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, @Valid @RequestBody RentalEditRequest request) {
//        service.editRental(principal.getMemberId(), id, request);
//    }

    @DeleteMapping("/{id}")
    public void deleteRental(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        service.deleteRental(principal.getMemberId(), id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/manage")
    public List<RentalResponse> rentalLog() {
        return service.getLog();
    }
}
