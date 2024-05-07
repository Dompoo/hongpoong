package Dompoo.Hongpoong.response.rental;

import Dompoo.Hongpoong.domain.Rental;
import lombok.Getter;

import java.time.LocalDate;

@Getter
/*
ResponseBody

<List조회시>
[
    {
        "id": 1,
        "product": "장구",
        "count": "1",
        "fromMember": "산틀",
        "toMember": "화랑",
        "date": "24/04/18",
        "time": 13
    },
    {
        "id": 2,
        "product": "장구",
        "count": "1",
        "fromMember": "산틀",
        "toMember": "화랑",
        "date": "24/04/18",
        "time": 13
    },
    . . .
]

<단건조회시>
{
    "id": 1,
    "product": "장구",
    "count": "1",
    "fromMember": "산틀",
    "toMember": "화랑",
    "date": "24/04/18",
    "time": 13
}
 */
public class RentalResponse {

    private Long id;
    private String product;
    private Integer count;
    private String requestMember;
    private String responseMember;
    private LocalDate date;
    private Integer time;

    public RentalResponse(Rental rental) {
        this.id = rental.getId();
        this.product = rental.getProduct();
        this.count = rental.getCount();
        this.requestMember = rental.getRequestMember().getUsername();
        this.responseMember = rental.getResponseMember().getUsername();
        this.date = rental.getDate();
        this.time = rental.getTime();
    }
}
