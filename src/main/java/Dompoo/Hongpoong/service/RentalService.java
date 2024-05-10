package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Instrument;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.domain.Reservation;
import Dompoo.Hongpoong.exception.*;
import Dompoo.Hongpoong.repository.InstrumentRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.repository.ReservationRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.response.rental.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;
    private final InstrumentRepository instrumentRepository;
    private final ReservationRepository reservationRepository;

    public void addRental(Long memberId, RentalCreateRequest request) {
        Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(ReservationNotFound::new);

        Rental rental = rentalRepository.save(Rental.builder()
                .reservation(reservation)
                .build());

        request.getInstrumentIds().stream()
                .map(instrumentRepository::findById)
                .forEach(instrument -> {
                    Instrument inst = instrument.orElseThrow(InstrumentNotFound::new);
                    //대여 가능한 악기만 대여
                    if(!inst.isAvailable()) throw new InstrumentNotAvailable();
                    //해당 악기를 대여할 수 없도록 수정
                    inst.setAvailable(false);
                    inst.setRental(rental);
                });
    }

    public RentalResponse getDetail(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFound::new);

        return new RentalResponse(rental);
    }

    public void deleteRental(Long memberId, Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFound::new);

        if (!rental.getReservation().getMember().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        //대여 끝나면 해당 악기를 대여 가능하도록 수정
        rental.getInstruments().forEach(instrument ->
                instrument.setAvailable(true));

        rentalRepository.delete(rental);
    }

    public List<RentalResponse> getLog() {
        return rentalRepository.findAll().stream()
                .map(RentalResponse::new)
                .collect(Collectors.toList());
    }
}
