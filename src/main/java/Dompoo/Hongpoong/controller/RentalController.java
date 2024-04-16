package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.UserPrincipal;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.request.rental.RentalEditRequest;
import Dompoo.Hongpoong.response.RentalResponse;
import Dompoo.Hongpoong.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rental")
public class RentalController {

    private final RentalService service;

    @GetMapping("")
    public List<RentalResponse> rentalMenu() {
        return service.getList();
    }

    @PostMapping("")
    public void addRental(UserPrincipal principal, @RequestBody @Valid RentalCreateRequest request) {
        service.addRental(principal.getMemberId(), request);
    }

    @GetMapping("/{id}")
    public RentalResponse rentalDetail(@PathVariable Long id) {
        return service.getDetail(id);
    }

    @PutMapping("/{id}")
    public void editRental(UserPrincipal principal, @PathVariable Long id, @Valid @RequestBody RentalEditRequest request) {
        service.editRental(principal.getMemberId(), id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(UserPrincipal principal, @PathVariable Long id) {
        service.deleteRental(principal.getMemberId(), id);
    }
}
