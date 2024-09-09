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
	private Boolean attend;
	
	@ManyToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@Builder
	private ReservationParticipate(Boolean attend, Reservation reservation, Member member) {
		this.attend = attend;
		this.reservation = reservation;
		this.member = member;
	}
	
	public static List<ReservationParticipate> of(Reservation reservation, List<Member> members) {
		return members.stream()
				.map(member -> ReservationParticipate.builder()
						.member(member)
						.reservation(reservation)
						.attend(false)
						.build())
				.toList();
	}
}
