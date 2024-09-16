package Dompoo.Hongpoong.domain.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInChatRoom {
	
	private final Long id;
	private final Member member;
	private final ChatRoom chatRoom;
}
