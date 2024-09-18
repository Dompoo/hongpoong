package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberInChatRoomJpaRepository extends JpaRepository<MemberInChatRoomJpaEntity, Long> {
	
	@Query("select m from MemberInChatRoomJpaEntity m where m.memberJpaEntity = :member")
	List<MemberInChatRoomJpaEntity> findAllByMember(MemberJpaEntity member);
	
//	void deleteByChatRoom(ChatRoomJpaEntity chatRoomJpaEntity);
	
	@Query("select count(m) > 0 from MemberInChatRoomJpaEntity m where m.memberJpaEntity = :member and m.chatRoomJpaEntity = :chatroom")
	boolean existsByMemberAndChatRoom(MemberJpaEntity member, ChatRoomJpaEntity chatroom);
	
	@Modifying
	@Query("delete from MemberInChatRoomJpaEntity m where m.memberJpaEntity = :member and m.chatRoomJpaEntity = :chatRoom")
	void deleteByMemberAndChatRoom(MemberJpaEntity member, ChatRoomJpaEntity chatRoom);
}
