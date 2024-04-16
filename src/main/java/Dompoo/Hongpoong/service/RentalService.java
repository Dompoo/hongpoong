package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.exception.DeleteFailException;
import Dompoo.Hongpoong.exception.EditFailException;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.exception.RentalNotFound;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.RentalRepository;
import Dompoo.Hongpoong.request.rental.RentalCreateRequest;
import Dompoo.Hongpoong.request.rental.RentalEditRequest;
import Dompoo.Hongpoong.response.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;

    public List<RentalResponse> getList() {
        return rentalRepository.findAll().stream()
                .map(RentalResponse::new)
                .collect(Collectors.toList());
    }

    public void addRental(Long memberId, RentalCreateRequest request) {
        Member toMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        Member fromMember = memberRepository.findByUsername(request.getFromMember())
                .orElseThrow(MemberNotFound::new);


        rentalRepository.save(Rental.builder()
                .product(request.getProduct())
                .count(request.getCount())
                .fromMember(fromMember)
                .toMember(toMember)
                .date(request.getDate())
                .time(request.getTime())
                .build());
    }

    public RentalResponse getDetail(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFound::new);

        return new RentalResponse(rental);
    }

    public void editRental(Long memberId, Long rentalId, RentalEditRequest request) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFound::new);

        if (!rental.getToMember().getId().equals(memberId)) {
            throw new EditFailException();
        }

        if (request.getFromMember() != null) {
            Member fromMember = memberRepository.findByUsername(request.getFromMember())
                    .orElseThrow(MemberNotFound::new);
            rental.setFromMember(fromMember);
        }
        if (request.getProduct() != null) rental.setProduct(request.getProduct());
        if (request.getCount() != null) rental.setCount(request.getCount());
        if (request.getDate() != null) rental.setDate(request.getDate());
        if (request.getTime() != null) rental.setTime(request.getTime());
    }

    public void deleteRental(Long memberId, Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFound::new);

        if (!rental.getToMember().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        rentalRepository.delete(rental);
    }
}
