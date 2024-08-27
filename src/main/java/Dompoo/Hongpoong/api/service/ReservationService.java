package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.api.dto.response.resevation.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.*;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> getList(ReservationSearchRequest request) {
        return reservationRepository.findAllByDate(request.getDate()).stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void addReservation(Long memberId, ReservationCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        if (request.getStartTime() > request.getEndTime()) {
            throw new EndForwardStart();
        }

        reservationRepository.save(request.toReservation(member));
    }

    @Transactional(readOnly = true)
    public ReservationResponse findReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        return ReservationResponse.from(reservation);
    }

    @Transactional
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

    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        reservationRepository.delete(reservation);
    }

    @Transactional
    public void edit(Long id, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationNotFound::new);

        reservation.edit(dto, now);
    }
}
