package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationEndResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.domain.Attendance;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.domain.ReservationEndImage;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.persistence.repository.AttendanceRepository;
import Dompoo.Hongpoong.domain.persistence.repository.MemberRepository;
import Dompoo.Hongpoong.domain.persistence.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberJpaRepository;
    
    @Transactional
    public ReservationDetailResponse createReservation(Long memberId, ReservationCreateRequest request, LocalDateTime now) {
        Member creator = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        List<Member> participaters = memberJpaRepository.findAllByIdIn(request.getParticipaterIds());
        
        ReservationTime.validateStartTimeAndEndTime(request.getStartTime(), request.getEndTime());
        
        if (isOverlapReservationExist(request)) {
            throw new ReservationOverlapException();
        }
        
        if (!participaters.contains(creator)) participaters.add(creator);
        
        Reservation savedReservation = reservationRepository.save(request.toReservation(creator, now));
        attendanceRepository.saveAll(Attendance.of(savedReservation, participaters));
        
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
        List<Attendance> attendances = attendanceRepository.findByMemberIdAndReservationDate(memberId, localDate);
        
        return ReservationResponse.fromAttendances(attendances);
    }
    
    @Transactional(readOnly = true)
    public ReservationDetailResponse findReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        List<Member> participators = attendanceRepository.findAllMemberByReservation(reservation);
        
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
        
        Reservation editedReservation = reservation.withExtendEndTime();
        reservationRepository.save(editedReservation);
    }
    
    @Transactional
    public void endReservation(Long memberId, Long reservationId, ReservationEndRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        reservationRepository.saveAllReservationEndImage(request.toReservationEndImages(reservation));
    }
    
    @Transactional(readOnly = true)
    public ReservationEndResponse findReservationEndDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        List<Attendance> participates = attendanceRepository.findAllByReservation(reservation);
        
        List<ReservationEndImage> images = reservationRepository.findAllEndImageByReservation(reservation);
        
        return ReservationEndResponse.of(reservation, participates, images);
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
        Reservation editedReservation = reservation.withEdited(dto.getDate(), dto.getStartTime(), dto.getEndTime(), dto.getMessage(), now);
        reservationRepository.save(editedReservation);
        
        List<Member> members = attendanceRepository.findAllMemberByReservation(reservation);
        
        return ReservationDetailResponse.of(reservation, members);
    }
    
    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new DeleteFailException();
        }
        
        attendanceRepository.deleteAllByReservation(reservation);
        reservationRepository.delete(reservation);
    }
    
    @Transactional
    public void editReservationByAdmin(Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        Reservation editedReservation = reservation.withEdited(dto.getDate(), dto.getStartTime(), dto.getEndTime(), dto.getMessage(), now);
        reservationRepository.save(editedReservation);
    }
    
    @Transactional
    public void deleteReservationByAdmin(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        attendanceRepository.deleteAllByReservation(reservation);
        reservationRepository.delete(reservation);
    }
    
    private boolean isOverlapReservationExist(ReservationCreateRequest request) {
        List<Reservation> reservations = reservationRepository.findAllByDate(request.getDate());
        
        for (Reservation reservation : reservations) {
            if (ReservationTime.isOverlap(reservation.getStartTime(), reservation.getEndTime(), request.getStartTime(), request.getEndTime()))
                return true;
        }
        
        return false;
    }
    
    private void updateAddedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getAddedParticipatorIds() != null) {
            List<Member> addedParticipators = memberJpaRepository.findAllByIdIn(dto.getAddedParticipatorIds());
            attendanceRepository.saveAll(Attendance.of(reservation, addedParticipators));
        }
    }
    
    private void updateDeletedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getRemovedParticipatorIds() != null) {
            List<Member> removedParticipators = memberJpaRepository.findAllByIdIn(dto.getRemovedParticipatorIds());
            attendanceRepository.deleteAllByReservationAndMemberIn(reservation, removedParticipators);
        }
    }
}
