package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInChatRoomJpaRepository extends JpaRepository<MemberInChatRoomJpaEntity, Long> {
	List<MemberInChatRoomJpaEntity> findAllByMember(MemberJpaEntity memberJpaEntity);
	
	void deleteByChatRoom(ChatRoomJpaEntity chatRoomJpaEntity);
	
	boolean existsByMemberAndChatRoom(MemberJpaEntity memberJpaEntity, ChatRoomJpaEntity chatroom);
}
