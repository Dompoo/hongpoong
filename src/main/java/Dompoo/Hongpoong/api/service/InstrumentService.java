package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditDto;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentDetailResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.repository.InstrumentBorrowRepository;
import Dompoo.Hongpoong.domain.repository.InstrumentRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final InstrumentBorrowRepository instrumentBorrowRepository;
    
    @Transactional
    public void createInstrument(Club club, InstrumentCreateRequest request) {
        instrumentRepository.save(request.toInstrument(club));
    }
    
    //member의 클럽과 다른 클럽 소속의 악기리스트 return
    @Transactional(readOnly = true)
    public List<InstrumentResponse> findAllOtherClubInstrument(Club club) {
        return instrumentRepository.findAllByClubNotEquals(club).stream()
                .map(InstrumentResponse::from)
                .toList();
    }
    
    //member의 클럽과 같은 클럽 소속의 악기리스트 return
    @Transactional(readOnly = true)
    public List<InstrumentResponse> findAllMyClubInstrument(Club club) {
        return instrumentRepository.findAllByClubEquals(club).stream()
                .map(InstrumentResponse::from)
                .toList();
    }

    @Transactional
    public void borrowInstrument(Long memberId, Long instrumentId, InstrumentBorrowRequest request, LocalDate now) {
        Reservation reservation = reservationRepository.findById(request.getReservationId()).orElseThrow(ReservationNotFound::new);
        Instrument instrument = instrumentRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        // 예약의 id 와 member id가 다르면 throw
        if (!reservation.getCreator().getId().equals(memberId)) throw new RentalFail();
        // 악기 대여가 불가능한 상태라면 throw
        if (!instrument.getAvailable()) throw new InstrumentNotAvailable();
        
        instrumentBorrowRepository.save(instrument.borrowInstrument(member, reservation, now));
    }

    @Transactional
    public void returnInstrument(Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFound::new);

        instrument.returnInstrument();
    }

    @Transactional(readOnly = true)
    public InstrumentDetailResponse findInstrumentDetail(Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        List<InstrumentBorrow> instrumentBorrows = instrumentBorrowRepository.findAllByInstrument(instrument);
        
        return InstrumentDetailResponse.from(instrument, instrumentBorrows);
    }

    @Transactional
    public void editInstrument(Club club, Long instrumentId, InstrumentEditDto dto) {
        Instrument instrument = instrumentRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        
        if (!instrument.getClub().equals(club)) throw new EditFailException();
        
        instrument.edit(dto);
    }

    @Transactional
    public void deleteInstrument(Club club, Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        
        if (!instrument.getClub().equals(club)) throw new DeleteFailException();

        instrumentRepository.delete(instrument);
    }
    
    @Transactional
    public void editInstrumentByAdmin(Long instrumentId, InstrumentEditDto dto) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFound::new);
        
        instrument.edit(dto);
    }
    
    @Transactional
    public void deleteInstrumentByAdmin(Long instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFound::new);
        
        instrumentRepository.delete(instrument);
    }
}
