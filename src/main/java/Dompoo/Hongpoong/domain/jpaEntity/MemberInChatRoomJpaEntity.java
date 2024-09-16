package Dompoo.Hongpoong.domain.jpaEntity;

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
	
	@ManyToOne @JoinColumn(name = "member_id")
	private MemberJpaEntity memberJpaEntity;
	
	@ManyToOne @JoinColumn(name = "chatroom_id")
	private ChatRoomJpaEntity chatRoomJpaEntity;
}
