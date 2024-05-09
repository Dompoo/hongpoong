package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository repository;
    private final MemberRepository memberRepository;

    public List<InstrumentResponse> getList() {
        return repository.findAll().stream()
                .map(InstrumentResponse::new)
                .collect(Collectors.toList());
    }

    public void addOne(Long memberId, InstrumentCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        repository.save(Instrument.builder()
                .product(request.getProduct())
                .member(member)
                .build());
    }

    public InstrumentResponse getOne(Long id) {
        return new InstrumentResponse(repository.findById(id)
                .orElseThrow(MemberNotFound::new));
    }
}
