package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.DeleteFailException;
import Dompoo.Hongpoong.exception.EditFailException;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository repository;
    private final MemberRepository memberRepository;

    //member의 클럽과 다른 클럽 소속의 악기리스트 return
    public List<InstrumentResponse> getListOther(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return repository.findAll().stream()
                .filter(instrument -> !instrument.getMember().getClub().equals(member.getClub()))
                .map(InstrumentResponse::new)
                .collect(Collectors.toList());
    }

    //member의 클럽과 같은 클럽 소속의 악기리스트 return
    public List<InstrumentResponse> getList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return repository.findAll().stream()
                .filter(instrument -> instrument.getMember().getClub().equals(member.getClub()))
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

    public void editOne(Long memberId, Long id, InstrumentEditRequest request) {
        Instrument instrument = repository.findById(id)
                .orElseThrow(MemberNotFound::new);

        if (!instrument.getMember().getId().equals(memberId)) throw new EditFailException();

        if (request.getProduct() != null) instrument.setProduct(request.getProduct());
        if (request.getAvailable() != null) instrument.setAvailable(request.getAvailable());
    }

    public void deleteOne(Long memberId, Long id) {
        Instrument instrument = repository.findById(id)
                .orElseThrow(MemberNotFound::new);

        if (!instrument.getMember().getId().equals(memberId)) throw new DeleteFailException();

        repository.delete(instrument);
    }
}
