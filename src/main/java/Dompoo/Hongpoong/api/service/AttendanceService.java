package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
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
        List<Attendance> attendances = participateRepository.findAllByReservationIdJoinFetchMember(reservationId);
        
        return AttendanceResponse.fromList(attendances);
    }
    
    @Transactional
    public AttendanceResponse attendReservation(Long memberId, Long reservationId, LocalDateTime now) {
        Reservation reservation = reservationJpaRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);
        
        Attendance participate = reservation.attendMember(now, () -> findOrCreateParticipate(memberId, reservation));
        
        return AttendanceResponse.from(participate);
    }
    
    @Transactional
    public List<AttendanceResponse> closeAttendance(Long memberId, Long reservationId) {
        Reservation reservation = reservationJpaRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        List<Attendance> attendances = participateRepository.findByReservationIdAndNotAttend(reservationId);
        attendances.forEach(rp -> rp.editAttendance(NO_SHOW));
        
        List<Attendance> resultAttendances = participateRepository.findAllByReservation(reservation);
        return AttendanceResponse.fromList(resultAttendances);
    }
    
    private Attendance findOrCreateParticipate(Long memberId, Reservation reservation) {
        return participateRepository.findByMemberIdAndReservationId(memberId, reservation.getId())
                .orElseGet(() -> createNewParticipate(memberId, reservation));
    }
    
    private Attendance createNewParticipate(Long memberId, Reservation reservation) {
        if (!reservation.getParticipationAvailable()) {
            throw new AttendanceNotFound();
        }
        
        Member member = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        return participateRepository.save(Attendance.of(reservation, member));
    }
}
