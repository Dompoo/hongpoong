package Dompoo.Hongpoong.api.controller;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.api.service.InstrumentService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instrument")
public class InstrumentController {

    private final InstrumentService service;

    @PostMapping
    @Secured(SecurePolicy.ADMIN_AND_LEADER)
    public void addOne(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentCreateRequest request) {
        service.addOne(claims.getId(), request);
    }

    @Secured
    @GetMapping
    public List<InstrumentResponse> getListFromOther(@LoginUser UserClaims claims) {
        return service.getListOther(claims.getId());
    }

    @Secured
    @GetMapping("/list")
    public List<InstrumentResponse> getList(@LoginUser UserClaims claims) {
        return service.getList(claims.getId());
    }

    @Secured
    @PostMapping("/borrow")
    public InstrumentBorrowResponse borrowOne(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentBorrowRequest request) {
        return service.borrowOne(claims.getId(), request);
    }

    @Secured
    @PostMapping("/return/{id}")
    public void returnOne(@PathVariable Long id) {
        service.returnOne(id);
    }

    @Secured
    @GetMapping("/{id}")
    public InstrumentResponse getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @Secured(SecurePolicy.ADMIN_AND_LEADER)
    @PutMapping("/{id}")
    public void edit(@LoginUser UserClaims claims, @PathVariable Long id, @RequestBody InstrumentEditRequest request) {
        service.editOne(claims.getId(), id, request.toDto());
    }

    @Secured(SecurePolicy.ADMIN_AND_LEADER)
    @DeleteMapping("/{id}")
    public void delete(@LoginUser UserClaims claims, @PathVariable Long id) {
        service.deleteOne(claims.getId(), id);

    }
}
