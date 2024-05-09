package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.service.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instrument")
public class InstrumentController {

    private final InstrumentService service;

    @PostMapping("")
    public void addOne(@AuthenticationPrincipal UserPrincipal principal, InstrumentCreateRequest request) {
        service.addOne(principal.getMemberId(), request);
    }

    @GetMapping("")
    public List<InstrumentResponse> getList() {
        return service.getList();
    }

    @GetMapping("/{id}")
    public InstrumentResponse getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @PutMapping("/{id}")
    public void edit(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id, InstrumentEditRequest request) {
        service.editOne(principal.getMemberId(), id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        service.deleteOne(principal.getMemberId(), id);

    }
}
