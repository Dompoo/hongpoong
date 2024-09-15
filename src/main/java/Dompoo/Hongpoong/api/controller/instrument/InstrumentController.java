package Dompoo.Hongpoong.api.controller.instrument;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentDetailResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.api.service.InstrumentService;
import Dompoo.Hongpoong.common.security.SecurePolicy;
import Dompoo.Hongpoong.common.security.UserClaims;
import Dompoo.Hongpoong.common.security.annotation.LoginUser;
import Dompoo.Hongpoong.common.security.annotation.Secured;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instrument")
public class InstrumentController implements InstrumentApi {
    
    private final InstrumentService service;
    
    @Secured(SecurePolicy.ADMIN_LEADER_PRIMARY)
    @PostMapping
    public void createInstrument(@LoginUser UserClaims claims, @RequestBody @Valid InstrumentCreateRequest request) {
        service.createInstrument(claims.getClub(), request);
    }

    @Secured
    @GetMapping
    public List<InstrumentResponse> findAllOtherClubInstrument(@LoginUser UserClaims claims) {
        return service.findAllOtherClubInstrument(claims.getClub());
    }

    @Secured
    @GetMapping("/list")
    public List<InstrumentResponse> findAllMyClubInstrument(@LoginUser UserClaims claims) {
        return service.findAllMyClubInstrument(claims.getClub());
    }

    @Secured
    @PostMapping("/{instrumentId}/borrow")
    public void borrowInstrument(@LoginUser UserClaims claims, @PathVariable Long instrumentId, @RequestBody @Valid InstrumentBorrowRequest request) {
        service.borrowInstrument(claims.getId(), instrumentId, request, LocalDate.now());
    }

    @Secured
    @PostMapping("/{instrumentId}/return")
    public void returnInstrument(@PathVariable Long instrumentId) {
        service.returnInstrument(instrumentId);
    }

    @Secured
    @GetMapping("/{instrumentId}")
    public InstrumentDetailResponse findInstrumentDetail(@PathVariable Long instrumentId) {
        return service.findInstrumentDetail(instrumentId);
    }

    @Secured(SecurePolicy.LEADER_PRIMARY)
    @PatchMapping("/{instrumentId}")
    public void editInstrument(@LoginUser UserClaims claims, @PathVariable Long instrumentId, @RequestBody InstrumentEditRequest request) {
        service.editInstrument(claims.getClub(), instrumentId, request.toDto());
    }
    
    @Secured(SecurePolicy.LEADER_PRIMARY)
    @DeleteMapping("/{instrumentId}")
    public void deleteInstrument(@LoginUser UserClaims claims, @PathVariable Long instrumentId) {
        service.deleteInstrument(claims.getClub(), instrumentId);
    }
    
    @Secured(SecurePolicy.ADMIN)
    @PatchMapping("/manage/{instrumentId}")
    public void editInstrumentByAdmin(@PathVariable Long instrumentId, @RequestBody InstrumentEditRequest request) {
        service.editInstrumentByAdmin(instrumentId, request.toDto());
    }
    
    @Secured(SecurePolicy.ADMIN)
    @DeleteMapping("/manage/{instrumentId}")
    public void deleteInstrumentByAdmin(@PathVariable Long instrumentId) {
        service.deleteInstrumentByAdmin(instrumentId);
    }
}
