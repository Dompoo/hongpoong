package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.chat.request.ChatMessageRequest;
import Dompoo.Hongpoong.api.dto.chat.request.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.chat.response.ChatMessageResponse;
import Dompoo.Hongpoong.api.dto.chat.response.ChatRoomResponse;
import Dompoo.Hongpoong.common.exception.impl.ChatRoomNotFound;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.jpaEntity.ChatMessageJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ChatMessageJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.ChatRoomJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberInChatRoomJpaRepository;
import Dompoo.Hongpoong.domain.persistence.jpaRepository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final MemberInChatRoomJpaRepository memberInChatRoomJpaRepository;
    
    @Transactional
    public ChatRoomResponse createRoom(ChatRoomCreateRequest request) {
        List<MemberJpaEntity> memberJpaEntities = memberJpaRepository.findAllByIdIn(request.getMemberIds());
        if (memberJpaEntities.size() != request.getMemberIds().size()) throw new MemberNotFound();
        
        ChatRoomJpaEntity savedRoom = chatRoomJpaRepository.save(request.toChatRoom());
        
        memberInChatRoomJpaRepository.saveAll(memberJpaEntities.stream()
                .map(member -> MemberInChatRoomJpaEntity.builder()
                        .memberJpaEntity(member)
                        .chatRoomJpaEntity(savedRoom)
                        .build())
                .toList());
        
        return ChatRoomResponse.of(savedRoom);
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllRoom(Long memberId) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        List<MemberInChatRoomJpaEntity> memberInChatRoomJpaEntities = memberInChatRoomJpaRepository.findAllByMember(memberJpaEntity);
        
        return ChatRoomResponse.fromList(memberInChatRoomJpaEntities);
    }

    @Transactional
    public void exitRoom(Long memberId, Long roomId) {
        MemberJpaEntity memberJpaEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        ChatRoomJpaEntity chatroom = chatRoomJpaRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);
        
        // 해당 멤버가 해당 채팅방에 있지 않다면 예외 발생
        if (!memberInChatRoomJpaRepository.existsByMemberAndChatRoom(memberJpaEntity, chatroom)) throw new DeleteFailException();
        
        memberInChatRoomJpaRepository.deleteByChatRoom(chatroom);
        chatroom.reduceMemberCount();
        
        // 멤버가 남아있지 않다면 채팅방도 삭제
        if (chatroom.getMemberCount() == 0) {
            chatRoomJpaRepository.delete(chatroom);
        }
    }
    
    @Transactional
    public ChatMessageResponse createChat(Long roomId, ChatMessageRequest request) {
        ChatRoomJpaEntity chatroom = chatRoomJpaRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);

        MemberJpaEntity sender = memberJpaRepository.findById(request.getSenderId())
                .orElseThrow(MemberNotFound::new);

        ChatMessageJpaEntity savedMessage = chatMessageJpaRepository.save(request.toChatMessage(chatroom, sender.getName()));
        
        return ChatMessageResponse.of(savedMessage, sender.getId());
    }
}
