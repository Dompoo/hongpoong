package Dompoo.Hongpoong.request.rental;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
/*
"품목": "장구",
"개수": "1",
"주는 멤버": "산틀",
"날짜": "24/04/18",
"시간": 17
 */
public class RentalEditRequest {

    private String product;
    private Integer count;
    private String fromMember;
    private LocalDate date;
    private Integer time;

    @Builder
    public RentalEditRequest(String product, Integer count, String fromMember, LocalDate date, Integer time) {
        this.product = product;
        this.count = count;
        this.fromMember = fromMember;
        this.date = date;
        this.time = time;
    }
}
