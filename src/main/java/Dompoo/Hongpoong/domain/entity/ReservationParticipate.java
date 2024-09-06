package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.domain.entity.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationParticipate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@Builder
	private ReservationParticipate(Reservation reservation, Member member) {
		this.reservation = reservation;
		this.member = member;
	}
	
	public static List<ReservationParticipate> of(Reservation reservation, List<Member> members) {
		return members.stream()
				.map(member -> ReservationParticipate.builder()
						.member(member)
						.reservation(reservation)
						.build())
				.toList();
	}
}
