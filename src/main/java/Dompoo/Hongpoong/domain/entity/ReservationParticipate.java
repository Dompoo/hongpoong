package Dompoo.Hongpoong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationParticipate {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Boolean attend;
	
	@ManyToOne @JoinColumn(name = "reservation_id")
	private Reservation reservation;
	
	@ManyToOne @JoinColumn(name = "member_id")
	private Member member;
	
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
