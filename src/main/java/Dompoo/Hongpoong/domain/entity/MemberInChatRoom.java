package Dompoo.Hongpoong.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInChatRoom {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne @JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne @JoinColumn(name = "chatroom_id")
	private ChatRoom chatRoom;
}
