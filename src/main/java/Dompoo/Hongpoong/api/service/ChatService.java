package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.chat.request.ChatMessageRequest;
import Dompoo.Hongpoong.api.dto.chat.request.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.chat.response.ChatMessageResponse;
import Dompoo.Hongpoong.api.dto.chat.response.ChatRoomResponse;
import Dompoo.Hongpoong.common.exception.impl.ChatRoomNotFound;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.entity.ChatMessage;
import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;
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
        List<Member> members = memberJpaRepository.findAllByIdIn(request.getMemberIds());
        if (members.size() != request.getMemberIds().size()) throw new MemberNotFound();
        
        ChatRoom savedRoom = chatRoomJpaRepository.save(request.toChatRoom());
        
        memberInChatRoomJpaRepository.saveAll(members.stream()
                .map(member -> MemberInChatRoom.builder()
                        .member(member)
                        .chatRoom(savedRoom)
                        .build())
                .toList());
        
        return ChatRoomResponse.of(savedRoom);
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllRoom(Long memberId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        List<MemberInChatRoom> memberInChatRooms = memberInChatRoomJpaRepository.findAllByMember(member);
        
        return ChatRoomResponse.fromList(memberInChatRooms);
    }

    @Transactional
    public void exitRoom(Long memberId, Long roomId) {
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        ChatRoom chatroom = chatRoomJpaRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);
        
        // 해당 멤버가 해당 채팅방에 있지 않다면 예외 발생
        if (!memberInChatRoomJpaRepository.existsByMemberAndChatRoom(member, chatroom)) throw new DeleteFailException();
        
        memberInChatRoomJpaRepository.deleteByChatRoom(chatroom);
        chatroom.reduceMemberCount();
        
        // 멤버가 남아있지 않다면 채팅방도 삭제
        if (chatroom.getMemberCount() == 0) {
            chatRoomJpaRepository.delete(chatroom);
        }
    }
    
    @Transactional
    public ChatMessageResponse createChat(Long roomId, ChatMessageRequest request) {
        ChatRoom chatroom = chatRoomJpaRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);

        Member sender = memberJpaRepository.findById(request.getSenderId())
                .orElseThrow(MemberNotFound::new);

        ChatMessage savedMessage = chatMessageJpaRepository.save(request.toChatMessage(chatroom, sender.getName()));
        
        return ChatMessageResponse.of(savedMessage, sender.getId());
    }
}
