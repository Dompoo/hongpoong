package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.request.Instrument.InstrumentEditDto;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentBorrowResponse;
import Dompoo.Hongpoong.api.dto.response.Instrument.InstrumentResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.InstrumentRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    
    @Transactional
    public void addOne(Long memberId, InstrumentCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        instrumentRepository.save(request.toInstrument(member));
    }
    
    //member의 클럽과 다른 클럽 소속의 악기리스트 return
    @Transactional(readOnly = true)
    public List<InstrumentResponse> getListOther(Long memberId) {
        Club club = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new)
                .getClub();
        
        return instrumentRepository.findAllByClubNotEquals(club).stream()
                .map(instrumet -> InstrumentResponse.from(instrumet, club))
                .toList();
    }
    
    //member의 클럽과 같은 클럽 소속의 악기리스트 return
    @Transactional(readOnly = true)
    public List<InstrumentResponse> getList(Long memberId) {
        Club club = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new)
                .getClub();

        return instrumentRepository.findAllByClubEquals(club).stream()
                .map(instrumet -> InstrumentResponse.from(instrumet, club))
                .toList();
    }

    @Transactional
    public InstrumentBorrowResponse borrowOne(Long memberId, InstrumentBorrowRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(ReservationNotFound::new);

        Instrument instrument = instrumentRepository.findById(request.getInstrumentId())
                .orElseThrow(InstrumentNotFound::new);

        // 예약의 id 와 member id가 다르면 throw
        if (!reservation.getMember().getId().equals(memberId)) throw new RentalFail();
        // 악기의 id 와 member id가 같으면 throw
        if (instrument.getMember().getId().equals(memberId)) throw new RentalFail();
        // 악기가 이미 대여중이면 throw
        if (!instrument.isAvailable()) throw new InstrumentNotAvailable();

        instrument.setReservation(reservation);
        instrument.setAvailable(false);

        return InstrumentBorrowResponse.from(instrument);
    }

    @Transactional
    public void returnOne(Long id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);

        instrument.setReservation(null);
        instrument.setAvailable(true);
    }

    @Transactional(readOnly = true)
    public InstrumentResponse getOne(Long id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);
        
        return InstrumentResponse.from(instrument, instrument.getMember().getClub());
    }

    @Transactional
    public void editOne(Long memberId, Long id, InstrumentEditDto dto) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);

        if (!instrument.getMember().getId().equals(memberId)) throw new EditFailException();
        
        instrument.edit(dto);
    }

    @Transactional
    public void deleteOne(Long memberId, Long id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(InstrumentNotFound::new);

        if (!instrument.getMember().getId().equals(memberId)) throw new DeleteFailException();

        instrumentRepository.delete(instrument);
    }
}
