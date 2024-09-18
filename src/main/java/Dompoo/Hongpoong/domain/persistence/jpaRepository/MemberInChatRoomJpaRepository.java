package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberInChatRoomJpaRepository extends JpaRepository<MemberInChatRoomJpaEntity, Long> {
	
	@Query("select m from MemberInChatRoomJpaEntity m where m.memberJpaEntity = :member")
	List<MemberInChatRoomJpaEntity> findAllByMember(
			@Param("member") MemberJpaEntity member
	);
	
	@Query("select count(m) > 0 from MemberInChatRoomJpaEntity m where m.memberJpaEntity = :member and m.chatRoomJpaEntity = :chatRoom")
	boolean existsByMemberAndChatRoom(
			@Param("member") MemberJpaEntity member,
			@Param("chatRoom") ChatRoomJpaEntity chatRoom
	);
	
	@Modifying
	@Query("delete from MemberInChatRoomJpaEntity m where m.memberJpaEntity = :member and m.chatRoomJpaEntity = :chatRoom")
	void deleteByMemberAndChatRoom(
			@Param("member") MemberJpaEntity member,
			@Param("chatRoom") ChatRoomJpaEntity chatRoom
	);
}
