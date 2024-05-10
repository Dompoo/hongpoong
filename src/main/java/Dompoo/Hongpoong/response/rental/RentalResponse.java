package Dompoo.Hongpoong.response.rental;

import Dompoo.Hongpoong.domain.Rental;
import Dompoo.Hongpoong.response.Instrument.InstrumentResponse;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<InstrumentResponse> instruments = new ArrayList<>();
    private String requestMember;
    private LocalDate date;
    private Integer time;

    public RentalResponse(Rental rental) {
        this.id = rental.getId();
        rental.getInstruments()
                .forEach(instrument -> instruments.add(new InstrumentResponse(instrument)));
        this.requestMember = rental.getRequestMember().getUsername();
        this.date = rental.getReservation().getDate();
        this.time = rental.getReservation().getTime();
    }
}
