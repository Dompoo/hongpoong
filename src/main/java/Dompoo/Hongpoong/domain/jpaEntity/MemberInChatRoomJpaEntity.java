package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.MemberInChatRoom;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInChatRoomJpaEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
	private MemberJpaEntity memberJpaEntity;
	
	@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "chatroom_id")
	private ChatRoomJpaEntity chatRoomJpaEntity;
	
	public MemberInChatRoom toDomain() {
		return MemberInChatRoom.builder()
				.id(this.id)
				.member(this.memberJpaEntity.toDomain())
				.chatRoom(this.chatRoomJpaEntity.toDomain())
				.build();
	}
	
	public static MemberInChatRoomJpaEntity of(MemberInChatRoom memberInChatRoom) {
		return MemberInChatRoomJpaEntity.builder()
				.id(memberInChatRoom.getId())
				.memberJpaEntity(MemberJpaEntity.of(memberInChatRoom.getMember()))
				.chatRoomJpaEntity(ChatRoomJpaEntity.of(memberInChatRoom.getChatRoom()))
				.build();
	}
}
