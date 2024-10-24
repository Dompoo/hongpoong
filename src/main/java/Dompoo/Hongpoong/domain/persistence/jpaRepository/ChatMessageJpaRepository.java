package Dompoo.Hongpoong.domain.persistence.jpaRepository;

import Dompoo.Hongpoong.domain.jpaEntity.ChatMessageJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {
	
	@Modifying
	@Query("delete from ChatMessageJpaEntity c where c.chatRoomJpaEntity = :chatRoom")
	void deleteByChatRoom(
			@Param("chatRoom") ChatRoomJpaEntity chatRoom
	);
}
