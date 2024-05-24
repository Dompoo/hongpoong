package Dompoo.Hongpoong.response.resevation;

import Dompoo.Hongpoong.domain.Reservation;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    "username": "화랑",
    "number": 15
    "date": "24/04/18",
    "time": 18,
    "message": "",
    "lastModified": ""
},
 */
public class ReservationResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final Integer number;
    private final LocalDate date;
    private final Integer startTime;
    private final Integer endTime;
    private final String message;
    private final LocalDateTime lastmodified;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.username = reservation.getMember().getUsername();
        this.email = reservation.getMember().getEmail();
        this.number = reservation.getNumber();
        this.date = reservation.getDate();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.message = reservation.getMessage();
        this.lastmodified = reservation.getLastModified();
    }
}
