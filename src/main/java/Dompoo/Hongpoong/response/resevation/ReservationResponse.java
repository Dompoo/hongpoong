package Dompoo.Hongpoong.response.resevation;

import Dompoo.Hongpoong.domain.Reservation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
/*
ResponseBody

<List조회시>
[
    {
        "id": 1,
        "username": "화랑",
        "date": "24/04/18",
        "time": 18
        "priority" : 1
    },
    {
        "id": 2,
        "username": "화랑",
        "date": "24/04/18",
        "time": 18
        "priority" : 1
    },
    . . .
]

<단건조회시>
{
    "id": 2,
    "date": "24/04/18",
    "username": "화랑",
    "time": 18
    "priority" : 1
},
 */
public class ReservationResponse {

    private final Long id;
    private final String username;
    private final LocalDate date;
    private final Integer time;
    private final Integer priority;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.username = reservation.getMember().getUsername();
        this.date = reservation.getDate();
        this.time = reservation.getTime();
        this.priority = reservation.getPriority();
    }
}
