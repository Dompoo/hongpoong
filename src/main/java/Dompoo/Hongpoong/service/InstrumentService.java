package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditRequest;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    //member의 클럽과 다른 클럽 소속의 악기리스트 return
    public List<InstrumentResponse> getListOther(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return instrumentRepository.findAll().stream()
                .filter(instrument -> !instrument.getMember().getClub().equals(member.getClub()))
                .map(InstrumentResponse::new)
                .collect(Collectors.toList());
    }

    //member의 클럽과 같은 클럽 소속의 악기리스트 return
    public List<InstrumentResponse> getList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return instrumentRepository.findAll().stream()
                .filter(instrument -> instrument.getMember().getClub().equals(member.getClub()))
                .map(InstrumentResponse::new)
                .collect(Collectors.toList());
    }

    public void addOne(Long memberId, InstrumentCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        instrumentRepository.save(Instrument.builder()
                .type(Instrument.InstrumentType.byInt(request.getType()))
                .member(member)
                .build());
    }

    public InstrumentBorrowResponse borrowOne(Long memberId, InstrumentBorrowRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(ReservationNotFound::new);

        Instrument instrument = instrumentRepository.findById(request.getInstrumentId())
                .orElseThrow(InstrumentNotFound::new);

        // 예약의 id 와 member id가 다르면 throw
        if (!Objects.equals(reservation.getMember().getId(), memberId)) throw new RentalFail();
        // 악기의 id 와 member id가 같으면 throw
        if (Objects.equals(instrument.getMember().getId(), memberId)) throw new RentalFail();
        // 악기가 이미 대여중이면 throw
        if (!instrument.isAvailable()) throw new InstrumentNotAvailable();

        instrument.setReservation(reservation);
        instrument.setAvailable(false);

        return new InstrumentBorrowResponse(instrument);
    }

    public void returnOne(Long id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);

        instrument.returnInstrument();
        instrument.setAvailable(true);
    }

    public InstrumentResponse getOne(Long id) {
        return new InstrumentResponse(instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new));
    }

    public void editOne(Long memberId, Long id, InstrumentEditRequest request) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);

        if (!instrument.getMember().getId().equals(memberId)) throw new EditFailException();

        if (request.getType() != null) instrument.setType(Instrument.InstrumentType.byInt(request.getType()));
        if (request.getAvailable() != null) instrument.setAvailable(request.getAvailable());
    }

    public void deleteOne(Long memberId, Long id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);

        if (!instrument.getMember().getId().equals(memberId)) throw new DeleteFailException();

        instrumentRepository.delete(instrument);
    }
}
