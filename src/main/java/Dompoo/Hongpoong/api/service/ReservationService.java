package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationEndResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.ReservationEndImageJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationJpaRepository reservationJpaRepository;
    private final AttendanceJpaRepository attendanceJpaRepository;
    private final ReservationEndImageJpaRepository reservationEndImageJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    
    @Transactional
    public ReservationDetailResponse createReservation(Long memberId, ReservationCreateRequest request, LocalDateTime now) {
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        List<Member> participaters = memberJpaRepository.findAllByIdIn(request.getParticipaterIds());
        
        ReservationTime.validateStartTimeAndEndTime(request.getStartTime(), request.getEndTime());
        
        if (isOverlapReservationExist(request)) {
            throw new ReservationOverlapException();
        }
        
        if (!participaters.contains(member)) participaters.add(member);
        
        Reservation savedReservation = reservationJpaRepository.save(request.toReservation(member, now));
        attendanceJpaRepository.saveAll(Attendance.of(savedReservation, participaters));
        
        return ReservationDetailResponse.of(savedReservation, participaters);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfYearAndMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        
        List<Reservation> reservations = reservationJpaRepository.findAllByDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        
        return ReservationResponse.fromList(reservations);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfDate(LocalDate date) {
        List<Reservation> reservations = reservationJpaRepository.findAllByDate(date);
        
        return ReservationResponse.fromList(reservations);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllTodoReservationOfToday(Long memberId, LocalDate localDate) {
        List<Attendance> attendances = attendanceJpaRepository.findByMemberIdAndReservationDate(memberId, localDate);
        
        return ReservationResponse.fromParticipateList(attendances);
    }
    
    @Transactional(readOnly = true)
    public ReservationDetailResponse findReservationDetail(Long reservationId) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        List<Member> participators = attendanceJpaRepository.findAllMemberByReservation(reservation);
        
        return ReservationDetailResponse.of(reservation, participators);
    }
    
    @Transactional
    public void extendReservationTime(Long memberId, Long reservationId, LocalTime now) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
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
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        reservationEndImageJpaRepository.saveAll(request.toReservationEndImages(reservation));
    }
    
    @Transactional(readOnly = true)
    public ReservationEndResponse findReservationEndDetail(Long reservationId) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        List<Attendance> participates = attendanceJpaRepository.findAllByReservation(reservation);
        
        List<ReservationEndImage> images = reservationEndImageJpaRepository.findAllByReservation(reservation);
        
        return ReservationEndResponse.of(reservation, participates, images);
    }
    
    @Transactional
    public ReservationDetailResponse editReservation(Long memberId, Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        ReservationTime.validateStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime());
        
        updateAddedParticipators(dto, reservation);
        updateDeletedParticipators(dto, reservation);
        reservation.edit(dto, now);
        
        List<Member> members = attendanceJpaRepository.findAllMemberByReservation(reservation);
        
        return ReservationDetailResponse.of(reservation, members);
    }
    
    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new DeleteFailException();
        }
        
        attendanceJpaRepository.deleteAllByReservation(reservation);
        reservationJpaRepository.delete(reservation);
    }
    
    @Transactional
    public void editReservationByAdmin(Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        reservation.edit(dto, now);
    }
    
    @Transactional
    public void deleteReservationByAdmin(Long reservationId) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        attendanceJpaRepository.deleteAllByReservation(reservation);
        reservationJpaRepository.delete(reservation);
    }
    
    private boolean isOverlapReservationExist(ReservationCreateRequest request) {
        List<Reservation> reservations = reservationJpaRepository.findAllByDate(request.getDate());
        
        for (Reservation reservation : reservations) {
            if (ReservationTime.isOverlap(reservation.getStartTime(), reservation.getEndTime(), request.getStartTime(), request.getEndTime()))
                return true;
        }
        
        return false;
    }
    
    private void updateAddedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getAddedParticipatorIds() != null) {
            List<Member> addedParticipators = memberJpaRepository.findAllByIdIn(dto.getAddedParticipatorIds());
            attendanceJpaRepository.saveAll(Attendance.of(reservation, addedParticipators));
        }
    }
    
    private void updateDeletedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getRemovedParticipatorIds() != null) {
            List<Member> removedParticipators = memberJpaRepository.findAllByIdIn(dto.getRemovedParticipatorIds());
            attendanceJpaRepository.deleteAllByReservationAndMemberIn(reservation, removedParticipators);
        }
    }
}
