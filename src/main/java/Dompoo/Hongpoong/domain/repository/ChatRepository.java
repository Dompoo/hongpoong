package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;

import java.util.List;

public interface ChatRepository {
	
	List<MemberInChatRoom> findAllByMember(Member member);
	
	void deleteByChatRoom(ChatRoom chatRoom);
	
	boolean existsByMemberAndChatRoom(Member member, ChatRoom chatroom);
}
