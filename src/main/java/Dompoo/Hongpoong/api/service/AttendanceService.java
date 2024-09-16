package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.AttendanceJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static Dompoo.Hongpoong.domain.enums.AttendanceStatus.NO_SHOW;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceJpaRepository participateRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    
    @Transactional(readOnly = true)
    public List<AttendanceResponse> findAttendance(Long reservationId) {
        List<AttendanceJpaEntity> attendanceJpaEntities = participateRepository.findAllByReservationIdJoinFetchMember(reservationId);
        
        return AttendanceResponse.fromList(attendanceJpaEntities);
    }
    
    @Transactional
    public AttendanceResponse attendReservation(Long memberId, Long reservationId, LocalDateTime now) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);
        
        AttendanceJpaEntity participate = reservationJpaEntity.attendMember(now, () -> findOrCreateParticipate(memberId, reservationJpaEntity));
        
        return AttendanceResponse.from(participate);
    }
    
    @Transactional
    public List<AttendanceResponse> closeAttendance(Long memberId, Long reservationId) {
        ReservationJpaEntity reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservationJpaEntity.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        List<AttendanceJpaEntity> attendanceJpaEntities = participateRepository.findByReservationIdAndNotAttend(reservationId);
        attendanceJpaEntities.forEach(rp -> rp.editAttendance(NO_SHOW));
        
        List<AttendanceJpaEntity> resultAttendanceJpaEntities = participateRepository.findAllByReservation(reservationJpaEntity);
        return AttendanceResponse.fromList(resultAttendanceJpaEntities);
    }
    
    private AttendanceJpaEntity findOrCreateParticipate(Long memberId, ReservationJpaEntity reservationJpaEntity) {
        return participateRepository.findByMemberIdAndReservationId(memberId, reservationJpaEntity.getId())
                .orElseGet(() -> createNewParticipate(memberId, reservationJpaEntity));
    }
    
    private AttendanceJpaEntity createNewParticipate(Long memberId, ReservationJpaEntity reservationJpaEntity) {
        if (!reservationJpaEntity.getParticipationAvailable()) {
            throw new AttendanceNotFound();
        }
        
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        return participateRepository.save(AttendanceJpaEntity.of(reservationJpaEntity, memberJpaEntity));
    }
}
