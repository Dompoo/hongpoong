package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.domain.ChatMessage;
import Dompoo.Hongpoong.domain.domain.ChatRoom;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.MemberInChatRoom;
import Dompoo.Hongpoong.domain.jpaEntity.ChatMessageJpaEntity;
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
import java.util.Optional;

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
	
	@Override
	public ChatRoom save(ChatRoom chatRoom) {
		return chatRoomJpaRepository.save(ChatRoomJpaEntity.of(chatRoom))
				.toDomain();
	}
	
	@Override
	public ChatMessage save(ChatMessage chatMessage) {
		return chatMessageJpaRepository.save(ChatMessageJpaEntity.of(chatMessage))
				.toDomain();
	}
	
	@Override
	public Optional<ChatRoom> findById(Long roomId) {
		return chatRoomJpaRepository.findById(roomId)
				.map(ChatRoomJpaEntity::toDomain);
	}
	
	@Override
	public void delete(ChatRoom reducedChatRoom) {
		chatMessageJpaRepository.deleteByChatRoom(ChatRoomJpaEntity.of(reducedChatRoom));
		chatRoomJpaRepository.delete(ChatRoomJpaEntity.of(reducedChatRoom));
	}
}
