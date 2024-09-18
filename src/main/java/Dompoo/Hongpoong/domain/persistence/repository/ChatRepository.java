package Dompoo.Hongpoong.domain.persistence.repository;

import Dompoo.Hongpoong.domain.domain.ChatMessage;
import Dompoo.Hongpoong.domain.domain.ChatRoom;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.MemberInChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
	
	List<MemberInChatRoom> findAllByMember(Member member);
	
	void deleteByChatRoom(ChatRoom chatRoom);
	
	boolean existsByMemberAndChatRoom(Member member, ChatRoom chatroom);
	
	ChatRoom save(ChatRoom chatRoom);
	
	ChatMessage save(ChatMessage chatMessage);
	
	Optional<ChatRoom> findById(Long roomId);
	
	void delete(ChatRoom reducedChatRoom);
}
