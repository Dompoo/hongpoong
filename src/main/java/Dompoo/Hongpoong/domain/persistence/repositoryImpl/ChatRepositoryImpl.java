package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.domain.ChatRoom;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.MemberInChatRoom;
import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ChatMessageJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ChatRoomJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberInChatRoomJpaRepository;
import Dompoo.Hongpoong.domain.persistence.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {
	
	private final ChatMessageJpaRepository chatMessageJpaRepository;
	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private final MemberInChatRoomJpaRepository memberInChatRoomJpaRepository;
	
	@Override
	public List<MemberInChatRoom> findAllByMember(Member member) {
		return memberInChatRoomJpaRepository.findAllByMember(MemberJpaEntity.of(member)).stream()
				.map(MemberInChatRoomJpaEntity::toDomain)
				.toList();
	}
	
	@Override
	public void deleteByChatRoom(ChatRoom chatRoom) {
		chatRoomJpaRepository.delete(ChatRoomJpaEntity.of(chatRoom));
	}
	
	@Override
	public boolean existsByMemberAndChatRoom(Member member, ChatRoom chatroom) {
		return memberInChatRoomJpaRepository.existsByMemberAndChatRoom(MemberJpaEntity.of(member), ChatRoomJpaEntity.of(chatroom));
	}
}
