package Dompoo.Hongpoong.domain.persistence.repositoryImpl;

import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
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
	public List<MemberInChatRoomJpaEntity> findAllByMember(MemberJpaEntity memberJpaEntity) {
		return memberInChatRoomJpaRepository.findAllByMember(memberJpaEntity);
	}
	
	@Override
	public void deleteByChatRoom(ChatRoomJpaEntity chatRoomJpaEntity) {
		memberInChatRoomJpaRepository.deleteByChatRoom(chatRoomJpaEntity);
	}
	
	@Override
	public boolean existsByMemberAndChatRoom(MemberJpaEntity memberJpaEntity, ChatRoomJpaEntity chatroom) {
		return memberInChatRoomJpaRepository.existsByMemberAndChatRoom(memberJpaEntity, chatroom);
	}
}
