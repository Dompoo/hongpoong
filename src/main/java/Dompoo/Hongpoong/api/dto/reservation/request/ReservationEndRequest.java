package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.domain.Reservation;
import Dompoo.Hongpoong.domain.domain.ReservationEndImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ReservationEndRequest {
    
    @Schema(example = "[image.com/1, image.com/2]")
    @NotNull(message = "연습실 사진은 필수입니다.")
    private final List<String> endImages;
    
    public List<ReservationEndImage> toReservationEndImages(Reservation reservation) {
        return endImages.stream().map(image -> ReservationEndImage.builder()
                        .imageUrl(image)
                        .reservation(reservation)
                        .build())
                .toList();
    }
}
