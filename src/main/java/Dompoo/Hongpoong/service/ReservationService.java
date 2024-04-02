package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.ReservationAheadShiftFail;
import Dompoo.Hongpoong.exception.ReservationNotFound;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.ReservationCreateRequest;
import Dompoo.Hongpoong.request.ReservationEditRequest;
import Dompoo.Hongpoong.request.ReservationShiftRequest;
import Dompoo.Hongpoong.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    public List<MenuResponse> getList() {
        return repository.findAll().stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * request를 받아서 해당 날의 모든 예약 목록중
     * 예약시간이 겹치는 것 개수 + 1을 예약 순서로 설정하여 저장한다.
     */
    public MenuResponse addReservation(ReservationCreateRequest request) {
        List<Reservation> findReservations = repository.findAllByDate(request.getDate());

        long overlapCount = findReservations.stream()
                .filter(r -> Objects.equals(r.getTime(), request.getTime()))
                .count();

        Reservation savedReservation = repository.save(Reservation.builder()
                .member(request.getMember())
                .date(request.getDate())
                .time(request.getTime())
                .priority((int) (overlapCount + 1))
                .build());

        return new MenuResponse(savedReservation);
    }

    public MenuResponse findReservation(Long id) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(ReservationNotFound::new);

        return new MenuResponse(reservation);
    }

    /**
     * id로 해당하는 예약을 찾고,
     * 현재 우선순위 ~ 원하는 우선순위 인 예약들의 우선순위를 +1
     * 해당하는 예약의 우선순위는 원하는 우선순위로.
     */
    public void shiftReservation(Long id, ReservationShiftRequest request) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(ReservationNotFound::new);

        if (reservation.getPriority() > request.getPriority()) {
            throw new ReservationAheadShiftFail();
        }

        List<Reservation> reservations = repository.findAllByDate(reservation.getDate());

        reservations.stream()
                .filter(r -> reservation.getPriority() < r.getPriority()
                        && r.getPriority() <= request.getPriority())
                .forEach(r -> r.setPriority(r.getPriority() - 1));

        reservation.setPriority(request.getPriority());
    }

    public void editReservation(Long id, ReservationEditRequest request) {
        Reservation reservation = repository.findById(id)
                .orElseThrow(ReservationNotFound::new);

        if (request.getDate() != null) reservation.setDate(request.getDate());
        if (request.getTime() != null) reservation.setTime(request.getTime());
    }

    public void deleteReservation(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ReservationNotFound();
        }
    }
}
