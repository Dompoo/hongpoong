package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.config.security.UserPrincipal;
import Dompoo.Hongpoong.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.service.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instrument")
public class InstrumentController {

    private final InstrumentService service;

    @GetMapping("")
    public List<InstrumentResponse> getList() {
        return service.getList();
    }

    @PostMapping("")
    public void addOne(@AuthenticationPrincipal UserPrincipal principal, InstrumentCreateRequest request) {
        service.addOne(principal.getMemberId(), request);
    }
}
