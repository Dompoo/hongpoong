package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.reservation.request.ReservationCreateRequest;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEditDto;
import Dompoo.Hongpoong.api.dto.reservation.request.ReservationEndRequest;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationDetailResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationEndResponse;
import Dompoo.Hongpoong.api.dto.reservation.response.ReservationResponse;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.EditFailException;
import Dompoo.Hongpoong.common.exception.impl.InstrumentNotAvailable;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationNotFound;
import Dompoo.Hongpoong.common.exception.impl.ReservationOverlapException;
import Dompoo.Hongpoong.common.exception.impl.ReturningInstrumentNotAvailable;
import Dompoo.Hongpoong.common.exception.impl.TimeExtendNotAvailableException;
import Dompoo.Hongpoong.domain.entity.Attendance;
import Dompoo.Hongpoong.domain.entity.Instrument;
import Dompoo.Hongpoong.domain.entity.InstrumentBorrow;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.Reservation;
import Dompoo.Hongpoong.domain.entity.ReservationEndImage;
import Dompoo.Hongpoong.domain.enums.ReservationTime;
import Dompoo.Hongpoong.domain.repository.AttendanceRepository;
import Dompoo.Hongpoong.domain.repository.InstrumentBorrowRepository;
import Dompoo.Hongpoong.domain.repository.InstrumentRepository;
import Dompoo.Hongpoong.domain.repository.MemberRepository;
import Dompoo.Hongpoong.domain.repository.ReservationEndImageRepository;
import Dompoo.Hongpoong.domain.repository.ReservationRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentBorrowRepository instrumentBorrowRepository;
    private final AttendanceRepository attendanceRepository;
    private final ReservationEndImageRepository reservationEndImageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReservationDetailResponse createReservation(Long memberId, ReservationCreateRequest request, LocalDateTime now) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
        List<Member> participaters = memberRepository.findAllByIdIn(request.getParticipaterIds());

        ReservationTime.validateStartTimeAndEndTime(request.getStartTime(), request.getEndTime());

        if (isOverlapReservationExist(request)) {
            throw new ReservationOverlapException();
        }

        if (!participaters.contains(member)) participaters.add(member);

        Reservation savedReservation = reservationRepository.save(request.toReservation(member, now));

        if (request.getBorrowInstrumentIds() != null) {
            List<Instrument> instruments = instrumentRepository.findAllById(request.getBorrowInstrumentIds());
            instruments.stream()
                    .peek(inst -> {
                        if (!inst.getAvailable()) {
                            throw new InstrumentNotAvailable();
                        }
                    }).forEach(inst -> instrumentBorrowRepository.save(inst.borrowInstrument(member, savedReservation, now.toLocalDate())));
        }

        attendanceRepository.saveAll(Attendance.of(savedReservation, participaters));

        return ReservationDetailResponse.of(savedReservation, participaters);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfYearAndMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        List<Reservation> reservations = reservationRepository.findAllByDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());

        return ReservationResponse.fromList(reservations);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservationOfDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByDate(date);

        return ReservationResponse.fromList(reservations);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllTodoReservationOfToday(Long memberId, LocalDate localDate) {
        List<Attendance> attendances = attendanceRepository.findByMemberIdAndReservationDate(memberId, localDate);

        return ReservationResponse.fromParticipateList(attendances);
    }

    @Transactional(readOnly = true)
    public ReservationDetailResponse findReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);
        List<Member> participators = attendanceRepository.findAllMemberByReservation(reservation);

        return ReservationDetailResponse.of(reservation, participators);
    }

    @Transactional
    public void extendReservationTime(Long memberId, Long reservationId, LocalTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }

        long minutes = Duration.between(now, reservation.getEndTime().localTime).toMinutes();
        if (0 > minutes || minutes > 30) { // 연습이 끝난 후거나 아직 30분 전이 되지 않았을 경우
            throw new TimeExtendNotAvailableException();
        }

        reservation.extendEndTime();
    }

    @Transactional
    public void endReservation(Long memberId, Long reservationId, ReservationEndRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }

        reservationEndImageRepository.saveAll(request.toReservationEndImages(reservation));
    }

    @Transactional(readOnly = true)
    public ReservationEndResponse findReservationEndDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        List<Attendance> participates = attendanceRepository.findAllByReservation(reservation);

        List<ReservationEndImage> images = reservationEndImageRepository.findAllByReservation(reservation);

        return ReservationEndResponse.of(reservation, participates, images);
    }

    @Transactional
    public ReservationDetailResponse editReservation(Long memberId, Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new EditFailException();
        }

        ReservationTime.validateStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime());

        updateAddedParticipators(dto, reservation);
        updateDeletedParticipators(dto, reservation);
        reservation.edit(dto, now);

        List<Long> addedInstrumentIds = dto.getAddedBorrowInstrumentIds();
        List<Long> removedInstrumentIds = dto.getRemovedBorrowInstrumentIds();
        if (addedInstrumentIds != null) {
            List<Instrument> addedInstrument = instrumentRepository.findAllById(addedInstrumentIds);
            addedInstrument.stream()
                    .peek(inst -> {
                        if (!inst.getAvailable()) {
                            throw new InstrumentNotAvailable();
                        }
                    }).forEach(inst ->
                            instrumentBorrowRepository.save(
                                    inst.borrowInstrument(
                                            reservation.getCreator(),
                                            reservation,
                                            now.toLocalDate()
                                    )));
        }
        if (removedInstrumentIds != null) {
            List<InstrumentBorrow> removedInstrument = instrumentBorrowRepository.findAllById(removedInstrumentIds);
            removedInstrument.forEach(instBorrow -> {
                        if (instBorrow.getReservation() != reservation) {
                            throw new ReturningInstrumentNotAvailable();
                        }
                        instBorrow.getInstrument().returnInstrument();
                    });
        }

        List<Member> members = attendanceRepository.findAllMemberByReservation(reservation);

        return ReservationDetailResponse.of(reservation, members);
    }

    @Transactional
    public void deleteReservation(Long memberId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        if (!reservation.getCreator().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        attendanceRepository.deleteAllByReservation(reservation);
        reservationRepository.delete(reservation);
    }

    @Transactional
    public void editReservationByAdmin(Long reservationId, ReservationEditDto dto, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        reservation.edit(dto, now);
    }

    @Transactional
    public void deleteReservationByAdmin(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(ReservationNotFound::new);

        attendanceRepository.deleteAllByReservation(reservation);
        reservationRepository.delete(reservation);
    }

    private boolean isOverlapReservationExist(ReservationCreateRequest request) {
        List<Reservation> reservations = reservationRepository.findAllByDate(request.getDate());

        for (Reservation reservation : reservations) {
            if (ReservationTime.isOverlap(reservation.getStartTime(), reservation.getEndTime(), request.getStartTime(), request.getEndTime()))
                return true;
        }

        return false;
    }

    private void updateAddedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getAddedParticipatorIds() != null) {
            List<Member> addedParticipators = memberRepository.findAllByIdIn(dto.getAddedParticipatorIds());
            attendanceRepository.saveAll(Attendance.of(reservation, addedParticipators));
        }
    }

    private void updateDeletedParticipators(ReservationEditDto dto, Reservation reservation) {
        if (dto.getRemovedParticipatorIds() != null) {
            List<Member> removedParticipators = memberRepository.findAllByIdIn(dto.getRemovedParticipatorIds());
            attendanceRepository.deleteAllByReservationAndMemberIn(reservation, removedParticipators);
        }
    }
}
