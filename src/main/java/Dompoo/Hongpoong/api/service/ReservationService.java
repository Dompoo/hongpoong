package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationEndImageRepository;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationParticipateRepository reservationParticipateRepository;
    private final ReservationEndImageRepository reservationEndImageRepository;
    private final MemberRepository memberRepository;
    
    @Transactional
    public ReservationDetailResponse createReservation(Long memberId, ReservationCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        List<Member> participaters = memberRepository.findAllByIdIn(request.getParticipaterIds());
        
        ReservationTime.validateStartTimeAndEndTime(request.getStartTime(), request.getEndTime());
        
        if (isOverlapReservationExist(request)) {
            throw new ReservationOverlapException();
        }
        
        if (!participaters.contains(member)) participaters.add(member);
        
        Reservation savedReservation = reservationRepository.save(request.toReservation(member));
        reservationParticipateRepository.saveAll(ReservationParticipate.of(savedReservation, participaters));
        
        return ReservationDetailResponse.of(savedReservation, participaters);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfYearAndMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        
        List<Reservation> reservations = reservationRepository.findAllByDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        
        return ReservationResponse.fromList(reservations);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByDate(date);
        
        return ReservationResponse.fromList(reservations);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllTodoReservationOfToday(Long memberId, LocalDate localDate) {
        List<ReservationParticipate> reservationParticipates = reservationParticipateRepository.findByMemberIdAndReservationDate(memberId, localDate);
        
        return ReservationResponse.fromParticipateList(reservationParticipates);
    }
    
    @Transactional(readOnly = true)
    public ReservationDetailResponse findReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        List<Member> participators = reservationParticipateRepository.findAllMemberByReservation(reservation);
        
        return ReservationDetailResponse.of(reservation, participators);
    }
    
    @Transactional
    public void extendReservationTime(Long memberId, Long reservationId, LocalTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        long minutes = Duration.between(now, reservation.getEndTime().localTime).toMinutes();
        if (0 > minutes || minutes > 30) { // 연습이 끝난 후거나 아직 30분 전이 되지 않았을 경우
            throw new TimeExtendNotAvailableException();
        }
        
        reservation.extendEndTime();
    }
    
    @Transactional
    public void endReservation(Long memberId, Long reservationId, ReservationEndRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        reservationEndImageRepository.saveAll(request.toReservationEndImages(reservation));
    }
    
    @Transactional
    public ReservationDetailResponse editReservation(Long memberId, Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        ReservationTime.validateStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime());
        
        updateAddedParticipators(dto, reservation);
        updateDeletedParticipators(dto, reservation);
        reservation.edit(dto, now);
        
        List<Member> members = reservationParticipateRepository.findAllMemberByReservation(reservation);
        
        return ReservationDetailResponse.of(reservation, members);
    }
    
    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new DeleteFailException();
        }
        
        reservationParticipateRepository.deleteAllByReservation(reservation);
        reservationRepository.delete(reservation);
    }
    
    @Transactional
    public void editReservationByAdmin(Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        reservation.edit(dto, now);
    }
    
    @Transactional
    public void deleteReservationByAdmin(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        reservationParticipateRepository.deleteAllByReservation(reservation);
        reservationRepository.delete(reservation);
    }
    
    private boolean isOverlapReservationExist(ReservationCreateRequest request) {
        List<Reservation> reservations = reservationRepository.findAllByDate(request.getDate());
        
        for (Reservation reservation : reservations) {
            if (reservation.getStartTime().isBetween(request.getStartTime().localTime, request.getEndTime().localTime)
                    || reservation.getEndTime().isBetween(request.getStartTime().localTime, request.getEndTime().localTime)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void updateAddedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getAddedParticipatorIds() != null) {
            List<Member> addedParticipators = memberRepository.findAllByIdIn(dto.getAddedParticipatorIds());
            reservationParticipateRepository.saveAll(ReservationParticipate.of(reservation, addedParticipators));
        }
    }
    
    private void updateDeletedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getRemovedParticipatorIds() != null) {
            List<Member> removedParticipators = memberRepository.findAllByIdIn(dto.getRemovedParticipatorIds());
            reservationParticipateRepository.deleteAllByReservationAndMemberIn(reservation, removedParticipators);
        }
    }
}
