package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.common.security.UserPrincipal;
import Dompoo.Hongpoong.service.InstrumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instrument")
public class InstrumentController {

    private final InstrumentService service;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LEADER')")
    public void addOne(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid InstrumentCreateRequest request) {
        service.addOne(principal.getMemberId(), request);
    }

    @GetMapping("")
    public List<InstrumentResponse> getListFromOther(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getListOther(principal.getMemberId());
    }

    @GetMapping("/list")
    public List<InstrumentResponse> getList(@AuthenticationPrincipal UserPrincipal principal) {
        return service.getList(principal.getMemberId());
    }

    @PostMapping("/borrow")
    public InstrumentBorrowResponse borrowOne(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid InstrumentBorrowRequest request) {
        return service.borrowOne(principal.getMemberId(), request);
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
    public void edit(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, @RequestBody InstrumentEditRequest request) {
        service.editOne(principal.getMemberId(), id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LEADER')")
    public void delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        service.deleteOne(principal.getMemberId(), id);

    }
}
