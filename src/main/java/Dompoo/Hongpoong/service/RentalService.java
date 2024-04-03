package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.exception.RentalNotFound;
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

    private final RentalRepository repository;

    public List<RentalResponse> getList() {
        return repository.findAll().stream()
                .map(RentalResponse::new)
                .collect(Collectors.toList());
    }

    public void addRental(RentalCreateRequest request) {
        repository.save(Rental.builder()
                .product(request.getProduct())
                .count(request.getCount())
                .fromMember(request.getFromMember())
                .toMember(request.getToMember())
                .date(request.getDate())
                .time(request.getTime())
                .build());
    }

    public RentalResponse getDetail(Long id) {
        Rental rental = repository.findById(id)
                .orElseThrow(RentalNotFound::new);

        return new RentalResponse(rental);
    }

    public void editRental(Long id, RentalEditRequest request) {
        Rental rental = repository.findById(id)
                .orElseThrow(RentalNotFound::new);

        if (request.getProduct() != null) rental.setProduct(request.getProduct());
        if (request.getCount() != null) rental.setCount(request.getCount());
        if (request.getFromMember() != null) rental.setFromMember(request.getFromMember());
        if (request.getDate() != null) rental.setDate(request.getDate());
        if (request.getTime() != null) rental.setTime(request.getTime());
    }

    public void deleteRental(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RentalNotFound();
        }
    }
}
