package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository repository;

    public List<InstrumentResponse> getList() {
        return repository.findAll().stream()
                .map(InstrumentResponse::new)
                .collect(Collectors.toList());
    }

    public void addOne() {

    }
}
