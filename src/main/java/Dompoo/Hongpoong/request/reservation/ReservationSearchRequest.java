package Dompoo.Hongpoong.request.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "date": "2024-12-18"
}
 */
public class ReservationSearchRequest {
    @NotNull(message = "날짜를 입력하세요.")
    private LocalDate date;

    @Builder
    public ReservationSearchRequest(LocalDate date) {
        this.date = date;
    }
}
