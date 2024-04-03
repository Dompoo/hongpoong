package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.request.rental.RentalEditRequest;
import Dompoo.Hongpoong.response.RentalResponse;
import Dompoo.Hongpoong.service.RentalService;
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
    public void addRental(@RequestBody RentalCreateRequest request) {
        service.addRental(request);
    }

    @GetMapping("/{id}")
    public RentalResponse rentalDetail(@PathVariable Long id) {
        return service.getDetail(id);
    }

    @PutMapping("/{id}")
    public void editRental(@PathVariable Long id, @RequestBody RentalEditRequest request) {
        service.editRental(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable Long id) {
        service.deleteRental(id);
    }
}
