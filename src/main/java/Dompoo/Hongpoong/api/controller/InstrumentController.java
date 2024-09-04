package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.api.service.InstrumentService;
import Dompoo.Hongpoong.common.security.LoginUser;
import Dompoo.Hongpoong.common.security.UserClaims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instrument")
public class InstrumentController {

    private final InstrumentService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LEADER')")
    public void addOne(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentCreateRequest request) {
        service.addOne(claims.getId(), request);
    }

    @GetMapping
    public List<InstrumentResponse> getListFromOther(@LoginUser UserClaims claims) {
        return service.getListOther(claims.getId());
    }

    @GetMapping("/list")
    public List<InstrumentResponse> getList(@LoginUser UserClaims claims) {
        return service.getList(claims.getId());
    }

    @PostMapping("/borrow")
    public InstrumentBorrowResponse borrowOne(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentBorrowRequest request) {
        return service.borrowOne(claims.getId(), request);
    }

    @PostMapping("/return/{id}")
    public void returnOne(@PathVariable Long id) {
        service.returnOne(id);
    }

    @GetMapping("/{id}")
    public InstrumentResponse getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LEADER')")
    public void edit(@LoginUser UserClaims claims, @PathVariable Long id, @RequestBody InstrumentEditRequest request) {
        service.editOne(claims.getId(), id, request.toDto());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LEADER')")
    public void delete(@LoginUser UserClaims claims, @PathVariable Long id) {
        service.deleteOne(claims.getId(), id);

    }
}
