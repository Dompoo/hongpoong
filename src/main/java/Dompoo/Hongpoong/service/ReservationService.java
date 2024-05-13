package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.reservation.ReservationCreateRequest;
import Dompoo.Hongpoong.request.reservation.ReservationEditRequest;
import Dompoo.Hongpoong.request.reservation.ReservationShiftRequest;
import Dompoo.Hongpoong.response.resevation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public List<ReservationResponse> getList() {
        return reservationRepository.findAll().stream()
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

        List<Reservation> findReservations = reservationRepository.findAllByDate(request.getDate());

        ArrayList<Reservation> newReservations = new ArrayList<>();

        for (Integer i = request.getStartTime(); i < request.getEndTime(); i++) {
            int count = 0;
            for (Reservation r : findReservations) {
                if (Objects.equals(r.getTime(), i)) count++;
            }

            newReservations.add(Reservation.builder()
                    .member(member)
                    .date(request.getDate())
                    .time(i)
                    .priority(count + 1)
                    .build());
        }

        reservationRepository.saveAll(newReservations);
    }

    public ReservationResponse findReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        return new ReservationResponse(reservation);
    }

    /**
     * id로 해당하는 예약을 찾고,
     * 현재 우선순위 ~ 원하는 우선순위 인 예약들의 우선순위를 +1
     * 해당하는 예약의 우선순위는 원하는 우선순위로.
     */
    public void shiftReservation(Long memberId, Long reservationId, ReservationShiftRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (reservation.getPriority() > request.getPriority()) {
            throw new ReservationAheadShiftFail();
        }

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new EditFailException();
        }

        List<Reservation> reservations = reservationRepository.findAllByDate(reservation.getDate());

        reservations.stream()
                .filter(r -> reservation.getPriority() < r.getPriority()
                        && r.getPriority() <= request.getPriority())
                .forEach(r -> r.setPriority(r.getPriority() - 1));

        reservation.setPriority(request.getPriority());
    }

    public void editReservation(Long memberId, Long reservationId, ReservationEditRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFound::new);

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new EditFailException();
        }

        if (request.getDate() != null) reservation.setDate(request.getDate());
        if (request.getTime() != null) reservation.setTime(request.getTime());
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
        if (request.getTime() != null) reservation.setTime(request.getTime());
    }
}
