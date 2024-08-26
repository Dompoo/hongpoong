package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInChatRoom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "chatroom_id")
	private ChatRoom chatRoom;
	
	@Builder
	public UserInChatRoom(Member member, ChatRoom chatRoom) {
		this.member = member;
		this.chatRoom = chatRoom;
	}
}
