package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.Rental;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RentalResponse {

    private Long id;
    private String product;
    private Integer count;
    private String fromMember;
    private String toMember;
    private LocalDate date;
    private Integer time;

    public RentalResponse(Rental rental) {
        this.id = rental.getId();
        this.product = rental.getProduct();
        this.count = rental.getCount();
        this.fromMember = rental.getFromMember().getUsername();
        this.toMember = rental.getToMember().getUsername();
        this.date = rental.getDate();
        this.time = rental.getTime();
    }
}
