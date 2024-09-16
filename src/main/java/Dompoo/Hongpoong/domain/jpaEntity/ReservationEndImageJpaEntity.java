package Dompoo.Hongpoong.domain.jpaEntity;

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
    
    @ManyToOne @JoinColumn(name = "reservation_id")
    private ReservationJpaEntity reservationJpaEntity;
}
