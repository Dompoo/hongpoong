package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationParticipateRepository reservationParticipateRepository;
    private final MemberRepository memberRepository;
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservationOfYearAndMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        
        List<Reservation> reservations = reservationRepository.findAllByDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        
        return ReservationResponse.fromList(reservations);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservationOfDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByDate(date);
        
        return ReservationResponse.fromList(reservations);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllTodoReservationOfToday(Long memberId, LocalDate localDate) {
        List<ReservationParticipate> reservationParticipates = reservationParticipateRepository.findByMemberIdAndReservationDate(memberId, localDate);
        
        return ReservationResponse.fromParticipateList(reservationParticipates);
    }
    
    @Transactional
    public void addReservation(Long memberId, ReservationCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        List<Member> participaters = memberRepository.findAllByIdIn(request.getParticipaterIds());
        
        ReservationTime.validateStartTimeAndEndTime(request.getStartTime(), request.getEndTime());
        
        Reservation savedReservation = reservationRepository.save(request.toReservation(member));
        reservationParticipateRepository.saveAll(ReservationParticipate.of(savedReservation, participaters));
    }
    
    @Transactional(readOnly = true)
    public ReservationResponse getReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        return ReservationResponse.from(reservation);
    }
    
    @Transactional
    public ReservationResponse editReservation(Long memberId, Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        ReservationTime.validateStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime());
        
        reservation.edit(dto, now);
        
        return ReservationResponse.from(reservation);
    }
    
    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        reservationRepository.delete(reservation);
    }
    
    @Transactional
    public void edit(Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        reservation.edit(dto, now);
    }
}
