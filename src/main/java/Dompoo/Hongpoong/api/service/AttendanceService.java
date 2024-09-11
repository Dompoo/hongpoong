package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.enums.Attendance;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static Dompoo.Hongpoong.domain.enums.Attendance.NO_SHOW;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final ReservationParticipateRepository reservationParticipateRepository;
    private final ReservationRepository reservationRepository;
    
    @Transactional(readOnly = true)
    public List<AttendanceResponse> findAttendance(Long reservationId) {
        List<ReservationParticipate> reservationParticipates = reservationParticipateRepository.findAllByReservationIdJoinFetchMember(reservationId);
        
        return AttendanceResponse.fromList(reservationParticipates);
    }
    
    @Transactional
    public AttendanceResponse attendReservation(Long memberId, Long reservationId, LocalDateTime now) {
        ReservationParticipate reservationParticipate = reservationParticipateRepository.findByMemberIdAndReservationId(memberId, reservationId).orElseThrow(AttendanceNotFound::new);
        
        boolean isLate = reservationParticipate.getReservation().getEndLocalDateTime().isBefore(now);
        
        reservationParticipate.editAttendance(isLate ? Attendance.LATE : Attendance.ATTEND);
        
        return AttendanceResponse.from(reservationParticipate);
    }
    
    @Transactional
    public List<AttendanceResponse> closeAttendance(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findByIdJoinFetchCreator(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        List<ReservationParticipate> reservationParticipates = reservationParticipateRepository.findByReservationIdAndNotAttend(reservationId);
        
        reservationParticipates.forEach(rp -> rp.editAttendance(NO_SHOW));
        
        return AttendanceResponse.fromList(reservationParticipates);
    }
}
