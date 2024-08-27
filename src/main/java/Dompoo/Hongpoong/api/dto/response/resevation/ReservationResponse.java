package Dompoo.Hongpoong.api.dto.response.resevation;

import Dompoo.Hongpoong.domain.entity.Reservation;
import lombok.Builder;
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
    
    @Builder
    private ReservationResponse(Long id, String username, String email, Integer number, LocalDate date, Integer startTime, Integer endTime, String message, LocalDateTime lastmodified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.number = number;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        this.lastmodified = lastmodified;
    }
    
    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .username(reservation.getMember().getUsername())
                .email(reservation.getMember().getEmail())
                .number(reservation.getNumber())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .message(reservation.getMessage())
                .lastmodified(reservation.getLastModified())
                .build();
    }
}
