package Dompoo.Hongpoong.api.dto.reservation.request;

import Dompoo.Hongpoong.domain.jpaEntity.ReservationJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ReservationEndImageJpaEntity;
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
    
    public List<ReservationEndImageJpaEntity> toReservationEndImages(ReservationJpaEntity reservationJpaEntity) {
        return endImages.stream().map(image -> ReservationEndImageJpaEntity.builder()
                        .imageUrl(image)
                        .reservationJpaEntity(reservationJpaEntity)
                        .build())
                .toList();
    }
}
