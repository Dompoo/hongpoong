package Dompoo.Hongpoong.domain.repository;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;
import Dompoo.Hongpoong.domain.jpaRepository.ChatMessageJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.ChatRoomJpaRepository;
import Dompoo.Hongpoong.domain.jpaRepository.MemberInChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository{
	
	private final ChatMessageJpaRepository chatMessageJpaRepository;
	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final MemberInChatRoomJpaRepository memberInChatRoomJpaRepository;
	
	@Override
	public List<MemberInChatRoom> findAllByMember(Member member) {
		return memberInChatRoomJpaRepository.findAllByMember(member);
	}
	
	@Override
	public void deleteByChatRoom(ChatRoom chatRoom) {
		memberInChatRoomJpaRepository.deleteByChatRoom(chatRoom);
	}
	
	@Override
	public boolean existsByMemberAndChatRoom(Member member, ChatRoom chatroom) {
		return memberInChatRoomJpaRepository.existsByMemberAndChatRoom(member, chatroom);
	}
}
