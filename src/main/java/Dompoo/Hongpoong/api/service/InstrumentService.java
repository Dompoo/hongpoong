package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentBorrowRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentCreateRequest;
import Dompoo.Hongpoong.api.dto.Instrument.request.InstrumentEditDto;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentDetailResponse;
import Dompoo.Hongpoong.api.dto.Instrument.response.InstrumentResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.InstrumentBorrowJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentBorrowJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.InstrumentJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrumentService {

    private final InstrumentJpaRepository instrumentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final InstrumentBorrowJpaRepository instrumentBorrowJpaRepository;
    
    @Transactional
    public void createInstrument(Club club, InstrumentCreateRequest request) {
        instrumentJpaRepository.save(request.toInstrument(club));
    }
    
    //member의 클럽과 다른 클럽 소속의 악기리스트 return
    @Transactional(readOnly = true)
    public List<InstrumentResponse> findAllOtherClubInstrument(Club club) {
        return instrumentJpaRepository.findAllByClubNotEquals(club).stream()
                .map(InstrumentResponse::from)
                .toList();
    }
    
    //member의 클럽과 같은 클럽 소속의 악기리스트 return
    @Transactional(readOnly = true)
    public List<InstrumentResponse> findAllMyClubInstrument(Club club) {
        return instrumentJpaRepository.findAllByClubEquals(club).stream()
                .map(InstrumentResponse::from)
                .toList();
    }

    @Transactional
    public void borrowInstrument(Long memberId, Long instrumentId, InstrumentBorrowRequest request, LocalDate now) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(request.getReservationId()).orElseThrow(ReservationNotFound::new);
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        
        // 예약의 id 와 member id가 다르면 throw
        if (!reservationJpaEntity.getCreator().getId().equals(memberId)) throw new RentalFail();
        // 악기 대여가 불가능한 상태라면 throw
        if (!instrumentJpaEntity.getAvailable()) throw new InstrumentNotAvailable();
        
        instrumentBorrowJpaRepository.save(instrumentJpaEntity.borrowInstrument(memberJpaEntity, reservationJpaEntity, now));
    }

    @Transactional
    public void returnInstrument(Long memberId, Long instrumentId) {
        InstrumentJpaEntity instrumentJpaEntity = instrumentBorrowJpaRepository.findByMemberIdAndInstrumentId(memberId, instrumentId)
                .orElseThrow(InstrumentNotFound::new);

        instrumentJpaEntity.returnInstrument();
    }

    @Transactional(readOnly = true)
    public InstrumentDetailResponse findInstrumentDetail(Long instrumentId) {
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        List<InstrumentBorrowJpaEntity> instrumentBorrowJpaEntities = instrumentBorrowJpaRepository.findAllByInstrument(instrumentJpaEntity);
        
        return InstrumentDetailResponse.from(instrumentJpaEntity, instrumentBorrowJpaEntities);
    }

    @Transactional
    public void editInstrument(Club club, Long instrumentId, InstrumentEditDto dto) {
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        
        if (!instrumentJpaEntity.getClub().equals(club)) throw new EditFailException();
        
        instrumentJpaEntity.edit(dto);
    }

    @Transactional
    public void deleteInstrument(Club club, Long instrumentId) {
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.findById(instrumentId).orElseThrow(InstrumentNotFound::new);
        
        if (!instrumentJpaEntity.getClub().equals(club)) throw new DeleteFailException();

        instrumentJpaRepository.delete(instrumentJpaEntity);
    }
    
    @Transactional
    public void editInstrumentByAdmin(Long instrumentId, InstrumentEditDto dto) {
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFound::new);
        
        instrumentJpaEntity.edit(dto);
    }
    
    @Transactional
    public void deleteInstrumentByAdmin(Long instrumentId) {
        InstrumentJpaEntity instrumentJpaEntity = instrumentJpaRepository.findById(instrumentId)
                .orElseThrow(InstrumentNotFound::new);
        
        instrumentJpaRepository.delete(instrumentJpaEntity);
    }
}
