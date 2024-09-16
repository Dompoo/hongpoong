package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationEndResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationEndImageJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
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
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        List<MemberJpaEntity> participaters = memberJpaRepository.findAllByIdIn(request.getParticipaterIds());
        
        ReservationTime.validateStartTimeAndEndTime(request.getStartTime(), request.getEndTime());
        
        if (isOverlapReservationExist(request)) {
            throw new ReservationOverlapException();
        }
        
        if (!participaters.contains(memberJpaEntity)) participaters.add(memberJpaEntity);
        
        ReservationJpaEntity savedReservationJpaEntity = reservationJpaRepository.save(request.toReservation(memberJpaEntity, now));
        attendanceJpaRepository.saveAll(AttendanceJpaEntity.of(savedReservationJpaEntity, participaters));
        
        return ReservationDetailResponse.of(savedReservationJpaEntity, participaters);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfYearAndMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        
        List<ReservationJpaEntity> reservationJpaEntities = reservationJpaRepository.findAllByDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());
        
        return ReservationResponse.fromList(reservationJpaEntities);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfDate(LocalDate date) {
        List<ReservationJpaEntity> reservationJpaEntities = reservationJpaRepository.findAllByDate(date);
        
        return ReservationResponse.fromList(reservationJpaEntities);
    }
    
    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllTodoReservationOfToday(Long memberId, LocalDate localDate) {
        List<AttendanceJpaEntity> attendanceJpaEntities = attendanceJpaRepository.findByMemberIdAndReservationDate(memberId, localDate);
        
        return ReservationResponse.fromParticipateList(attendanceJpaEntities);
    }
    
    @Transactional(readOnly = true)
    public ReservationDetailResponse findReservationDetail(Long reservationId) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        List<MemberJpaEntity> participators = attendanceJpaRepository.findAllMemberByReservation(reservationJpaEntity);
        
        return ReservationDetailResponse.of(reservationJpaEntity, participators);
    }
    
    @Transactional
    public void extendReservationTime(Long memberId, Long reservationId, LocalTime now) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservationJpaEntity.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        long minutes = Duration.between(now, reservationJpaEntity.getEndTime().localTime).toMinutes();
        if (0 > minutes || minutes > 30) { // 연습이 끝난 후거나 아직 30분 전이 되지 않았을 경우
            throw new TimeExtendNotAvailableException();
        }
        
        reservationJpaEntity.extendEndTime();
    }
    
    @Transactional
    public void endReservation(Long memberId, Long reservationId, ReservationEndRequest request) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservationJpaEntity.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        reservationEndImageJpaRepository.saveAll(request.toReservationEndImages(reservationJpaEntity));
    }
    
    @Transactional(readOnly = true)
    public ReservationEndResponse findReservationEndDetail(Long reservationId) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        List<AttendanceJpaEntity> participates = attendanceJpaRepository.findAllByReservation(reservationJpaEntity);
        
        List<ReservationEndImageJpaEntity> images = reservationEndImageJpaRepository.findAllByReservation(reservationJpaEntity);
        
        return ReservationEndResponse.of(reservationJpaEntity, participates, images);
    }
    
    @Transactional
    public ReservationDetailResponse editReservation(Long memberId, Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservationJpaEntity.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        ReservationTime.validateStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime());
        
        updateAddedParticipators(dto, reservationJpaEntity);
        updateDeletedParticipators(dto, reservationJpaEntity);
        reservationJpaEntity.edit(dto, now);
        
        List<MemberJpaEntity> memberJpaEntities = attendanceJpaRepository.findAllMemberByReservation(reservationJpaEntity);
        
        return ReservationDetailResponse.of(reservationJpaEntity, memberJpaEntities);
    }
    
    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservationJpaEntity.getCreator().getId().equals(memberId)) {
            throw new DeleteFailException();
        }
        
        attendanceJpaRepository.deleteAllByReservation(reservationJpaEntity);
        reservationJpaRepository.delete(reservationJpaEntity);
    }
    
    @Transactional
    public void editReservationByAdmin(Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        reservationJpaEntity.edit(dto, now);
    }
    
    @Transactional
    public void deleteReservationByAdmin(Long reservationId) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        attendanceJpaRepository.deleteAllByReservation(reservationJpaEntity);
        reservationJpaRepository.delete(reservationJpaEntity);
    }
    
    private boolean isOverlapReservationExist(ReservationCreateRequest request) {
        List<ReservationJpaEntity> reservationJpaEntities = reservationJpaRepository.findAllByDate(request.getDate());
        
        for (ReservationJpaEntity reservationJpaEntity : reservationJpaEntities) {
            if (ReservationTime.isOverlap(reservationJpaEntity.getStartTime(), reservationJpaEntity.getEndTime(), request.getStartTime(), request.getEndTime()))
                return true;
        }
        
        return false;
    }
    
    private void updateAddedParticipators(ReservationEditDto dto, ReservationJpaEntity reservationJpaEntity) {
        if (dto.getAddedParticipatorIds() != null) {
            List<MemberJpaEntity> addedParticipators = memberJpaRepository.findAllByIdIn(dto.getAddedParticipatorIds());
            attendanceJpaRepository.saveAll(AttendanceJpaEntity.of(reservationJpaEntity, addedParticipators));
        }
    }
    
    private void updateDeletedParticipators(ReservationEditDto dto, ReservationJpaEntity reservationJpaEntity) {
        if (dto.getRemovedParticipatorIds() != null) {
            List<MemberJpaEntity> removedParticipators = memberJpaRepository.findAllByIdIn(dto.getRemovedParticipatorIds());
            attendanceJpaRepository.deleteAllByReservationAndMemberIn(reservationJpaEntity, removedParticipators);
        }
    }
}
