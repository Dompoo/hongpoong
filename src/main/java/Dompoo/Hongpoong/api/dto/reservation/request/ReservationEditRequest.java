package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.api.dto.reservation.ReservationEditDto;
import Dompoo.Hongpoong.domain.entity.reservation.ReservationTime;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ReservationEditRequest {

    private final Integer number;

    @FutureOrPresent(message = "과거 날짜일 수 없습니다.")
    private final LocalDate date;
    
    @NotBlank(message = "시작 시간을 입력하세요.")
    private final String startTime;
    
    @NotBlank(message = "종료 시간을 입력하세요.")
    private final String endTime;

    private final String message = "";
    
    public ReservationEditDto toDto() {
        return ReservationEditDto.builder()
                .number(number)
                .date(date)
                .startTime(ReservationTime.from(startTime))
                .endTime(ReservationTime.from(endTime))
                .message(message)
                .build();
    }
}
