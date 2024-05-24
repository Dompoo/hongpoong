package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.request.reservation.ReservationSearchRequest;
import Dompoo.Hongpoong.response.resevation.ReservationResponse;
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
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * request를 받아서 해당 날의 모든 예약 목록중
     * 예약시간이 겹치는 것 개수 + 1을 예약 순서로 설정하여 저장한다.
     */
    public void addReservation(Long memberId, ReservationCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        if (request.getStartTime() > request.getEndTime()) {
            throw new EndForwardStart();
        }

        reservationRepository.save(Reservation.builder()
                .member(member)
                .number(request.getNumber())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .message(request.getMessage())
                .build());
    }

    public ReservationResponse findReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        return new ReservationResponse(reservation);
    }

    public void editReservation(Long memberId, Long reservationId, ReservationEditRequest request) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new EditFailException();
        }

        if (request.getStartTime() > request.getEndTime()) {
            throw new EndForwardStart();
        }

        if (request.getDate() != null) reservation.setDate(request.getDate());
        if (request.getStartTime() != null) reservation.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) reservation.setEndTime(request.getEndTime());

        reservation.setLastModified(LocalDateTime.now());
    }

    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        reservationRepository.delete(reservation);
    }

    public void edit(Long id, ReservationEditRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationNotFound::new);

        if (request.getDate() != null) reservation.setDate(request.getDate());
        if (request.getStartTime() != null) reservation.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) reservation.setEndTime(request.getEndTime());
    }
}
