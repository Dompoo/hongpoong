package Dompoo.Hongpoong.api.controller.instrument;

import Dompoo.Hongpoong.api.dto.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
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
public class InstrumentController implements InstrumentApi {

    private final InstrumentService service;

    @PostMapping
    @Secured(SecurePolicy.ADMIN_AND_LEADER)
    public void createInstrument(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentCreateRequest request) {
        service.createInstrument(claims.getId(), request);
    }

    @Secured
    @GetMapping
    public List<InstrumentResponse> findAllOtherClubInstrument(@LoginUser UserClaims claims) {
        return service.findAllOtherClubInstrument(claims.getId());
    }

    @Secured
    @GetMapping("/list")
    public List<InstrumentResponse> findAllMyClubInstrument(@LoginUser UserClaims claims) {
        return service.findAllMyClubInstrument(claims.getId());
    }

    @Secured
    @PostMapping("/borrow")
    public InstrumentBorrowResponse borrowInstrument(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentBorrowRequest request) {
        return service.borrowInstrument(claims.getId(), request);
    }

    @Secured
    @PostMapping("/return/{instrumentId}")
    public void returnInstrument(@PathVariable Long instrumentId) {
        service.returnInstrument(instrumentId);
    }

    @Secured
    @GetMapping("/{instrumentId}")
    public InstrumentResponse findInstrumentDetail(@PathVariable Long instrumentId) {
        return service.findInstrumentDetail(instrumentId);
    }

    @Secured(SecurePolicy.ADMIN_AND_LEADER)
    @PutMapping("/{instrumentId}")
    public void editInstrument(@LoginUser UserClaims claims, @PathVariable Long instrumentId, @RequestBody InstrumentEditRequest request) {
        service.editInstrument(claims.getId(), instrumentId, request.toDto());
    }

    @Secured(SecurePolicy.ADMIN_AND_LEADER)
    @DeleteMapping("/{instrumentId}")
    public void deleteInstrument(@LoginUser UserClaims claims, @PathVariable Long instrumentId) {
        service.deleteInstrument(claims.getId(), instrumentId);
    }
}
