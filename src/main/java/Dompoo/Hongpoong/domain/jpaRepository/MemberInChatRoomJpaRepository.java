package Dompoo.Hongpoong.domain.jpaRepository;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInChatRoomJpaRepository extends JpaRepository<MemberInChatRoom, Long> {
	List<MemberInChatRoom> findAllByMember(Member member);
	
	void deleteByChatRoom(ChatRoom chatRoom);
	
	boolean existsByMemberAndChatRoom(Member member, ChatRoom chatroom);
}
