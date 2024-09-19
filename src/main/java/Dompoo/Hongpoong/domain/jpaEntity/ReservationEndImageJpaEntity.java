package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.ReservationEndImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationEndImageJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String imageUrl;
    
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "reservation_id")
    private ReservationJpaEntity reservationJpaEntity;
    
    public ReservationEndImage toDomain() {
        return ReservationEndImage.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .reservation(this.reservationJpaEntity.toDomain())
                .build();
    }
    
    public static ReservationEndImageJpaEntity of(ReservationEndImage reservationEndImage) {
        return ReservationEndImageJpaEntity.builder()
                .id(reservationEndImage.getId())
                .imageUrl(reservationEndImage.getImageUrl())
                .reservationJpaEntity(ReservationJpaEntity.of(reservationEndImage.getReservation()))
                .build();
    }
}
