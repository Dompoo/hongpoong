package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public List<ReservationResponse> getList(ReservationSearchRequest request) {
        return reservationRepository.findAllByDate(request.getDate()).stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }
    
    public void addReservation(Long memberId, ReservationCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        if (request.getStartTime() > request.getEndTime()) {
            throw new EndForwardStart();
        }

        reservationRepository.save(request.toReservation(member));
    }

    public ReservationResponse findReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        return ReservationResponse.from(reservation);
    }

    public ReservationResponse editReservation(Long memberId, Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new EditFailException();
        }

        if (dto.getStartTime() > dto.getEndTime()) {
            throw new EndForwardStart();
        }
        
        reservation.edit(dto, now);
        
        return ReservationResponse.from(reservation);
    }

    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        reservationRepository.delete(reservation);
    }

    public void edit(Long id, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationNotFound::new);

        reservation.edit(dto, now);
    }
}
