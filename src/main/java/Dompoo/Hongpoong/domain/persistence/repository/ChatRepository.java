package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;

import java.util.List;

public interface ChatRepository {
	
	List<MemberInChatRoomJpaEntity> findAllByMember(MemberJpaEntity memberJpaEntity);
	
	void deleteByChatRoom(ChatRoomJpaEntity chatRoomJpaEntity);
	
	boolean existsByMemberAndChatRoom(MemberJpaEntity memberJpaEntity, ChatRoomJpaEntity chatroom);
}
