package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.attendance.AttendanceResponse;
import Dompoo.Hongpoong.api.dto.attendance.AttendanceResultResponse;
import Dompoo.Hongpoong.common.exception.impl.AttendanceNotFound;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.domain.domain.Attendance;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.persistence.repository.AttendanceRepository;
import Dompoo.Hongpoong.domain.persistence.repository.MemberRepository;
import Dompoo.Hongpoong.domain.persistence.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static Dompoo.Hongpoong.domain.enums.AttendanceStatus.NO_SHOW;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    
    @Transactional(readOnly = true)
    public List<AttendanceResponse> findAttendance(Long reservationId) {
        List<Attendance> attendances = attendanceRepository.findAllByReservationIdJoinFetchMember(reservationId);
        
        return AttendanceResponse.fromList(attendances);
    }
    
    @Transactional
    public AttendanceResultResponse attendReservation(Long memberId, Long reservationId, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (reservation.getParticipationAvailable()) {
            Optional<Attendance> prevAttendance = attendanceRepository.findByMemberIdAndReservationId(memberId, reservationId);
            
            if (prevAttendance.isPresent()) {
                Attendance currAttendance = prevAttendance.get().withAttendance(reservation.getStartTime(), now);
                attendanceRepository.save(currAttendance);
                return AttendanceResultResponse.from(currAttendance);
            } else {
                Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
                Attendance currAttendance = attendanceRepository.save(Attendance.of(reservation, member).withAttendance(reservation.getStartTime(), now));
                return AttendanceResultResponse.from(currAttendance);
            }
            
        } else {
            Attendance prevAttendance = attendanceRepository.findByMemberIdAndReservationId(memberId, reservationId).orElseThrow(AttendanceNotFound::new);
            Attendance currAttendance = prevAttendance.withAttendance(reservation.getStartTime(), now);
            
            attendanceRepository.save(currAttendance);
            return AttendanceResultResponse.from(currAttendance);
        }
    }
    
    @Transactional
    public void closeAttendance(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        
        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }
        
        List<Attendance> attendances = attendanceRepository.findByReservationIdAndNotAttend(reservationId).stream()
                .map(rp -> rp.withAttendance(NO_SHOW))
                .toList();
        
        attendanceRepository.saveAll(attendances);
    }
}
