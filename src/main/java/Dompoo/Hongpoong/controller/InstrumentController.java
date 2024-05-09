package Dompoo.Hongpoong.controller;

import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.service.InstrumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
