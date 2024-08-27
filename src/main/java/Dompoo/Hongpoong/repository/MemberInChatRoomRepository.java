package Dompoo.Hongpoong.repository;

import Dompoo.Hongpoong.domain.ChatRoom;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.MemberInChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInChatRoomRepository extends JpaRepository<MemberInChatRoom, Long> {
	List<MemberInChatRoom> findAllByMember(Member member);
	
	void deleteByChatRoom(ChatRoom chatRoom);
	
	boolean existsByMemberAndChatRoom(Member member, ChatRoom chatroom);
}
