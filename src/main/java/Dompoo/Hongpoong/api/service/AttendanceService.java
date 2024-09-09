package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.member.response.MemberResponse;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import Dompoo.Hongpoong.domain.repository.ReservationParticipateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final ReservationParticipateRepository reservationParticipateRepository;
    
    @Transactional(readOnly = true)
    public List<MemberResponse> findAllNotAttendedMember(Long reservationId) {
        List<ReservationParticipate> reservationParticipates = reservationParticipateRepository.findAllByReservationIdAndNotAttend(reservationId);
        
        return MemberResponse.fromParticipates(reservationParticipates);
    }
}
