package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.exception.*;
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

    public List<RentalResponse> getList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        return rentalRepository.findAll().stream()
                .filter(rental -> rental.getRequestMember().getClub().equals(member.getClub()) || rental.getResponseMember().getClub().equals(member.getClub()))
                .map(RentalResponse::new)
                .collect(Collectors.toList());
    }

    public void addRental(Long memberId, RentalCreateRequest request) {
        Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        Member responseMember = memberRepository.findByUsername(request.getResponseMember())
                .orElseThrow(MemberNotFound::new);

        if (requestMember.getId().equals(responseMember.getId())) {
            throw new SelfRentalException();
        }

        rentalRepository.save(Rental.builder()
                .product(request.getProduct())
                .count(request.getCount())
                .requestMember(requestMember)
                .responseMember(responseMember)
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

        if (!rental.getRequestMember().getId().equals(memberId)) {
            throw new EditFailException();
        }

        if (request.getResponseMember() != null) {
            Member fromMember = memberRepository.findByUsername(request.getResponseMember())
                    .orElseThrow(MemberNotFound::new);
            rental.setResponseMember(fromMember);
        }
        if (request.getProduct() != null) rental.setProduct(request.getProduct());
        if (request.getCount() != null) rental.setCount(request.getCount());
        if (request.getDate() != null) rental.setDate(request.getDate());
        if (request.getTime() != null) rental.setTime(request.getTime());
    }

    public void deleteRental(Long memberId, Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(RentalNotFound::new);

        if (!rental.getRequestMember().getId().equals(memberId)) {
            throw new DeleteFailException();
        }

        rentalRepository.delete(rental);
    }

    public List<RentalResponse> getLog() {
        return rentalRepository.findAll().stream()
                .map(RentalResponse::new)
                .collect(Collectors.toList());
    }
}
