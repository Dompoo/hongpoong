package Dompoo.Hongpoong.api.service;

import Dompoo.Hongpoong.api.dto.chat.request.ChatMessageRequest;
import Dompoo.Hongpoong.api.dto.chat.request.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.chat.response.ChatMessageResponse;
import Dompoo.Hongpoong.api.dto.chat.response.ChatRoomResponse;
import Dompoo.Hongpoong.common.exception.impl.ChatRoomNotFound;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.domain.ChatMessage;
import Dompoo.Hongpoong.domain.domain.ChatRoom;
import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.domain.MemberInChatRoom;
import Dompoo.Hongpoong.domain.persistence.repository.ChatRepository;
import Dompoo.Hongpoong.domain.persistence.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    
    @Transactional
    public ChatRoomResponse createRoom(ChatRoomCreateRequest request) {
        List<Member> members = memberRepository.findAllByIdIn(request.getMemberIds());
        
        if (members.size() != request.getMemberIds().size()) throw new MemberNotFound();
        
        ChatRoom savedRoom = chatRepository.save(request.toChatRoom());
        
        memberRepository.saveAll(members.stream()
                .map(member -> MemberInChatRoom.builder()
                        .member(member)
                        .chatRoom(savedRoom)
                        .build())
                .toList());
        
        return ChatRoomResponse.of(savedRoom);
    }
    
    @Transactional
    public ChatMessageResponse createChat(Long roomId, ChatMessageRequest request) {
        ChatRoom chatroom = chatRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);
        
        Member sender = memberRepository.findById(request.getSenderId())
                .orElseThrow(MemberNotFound::new);
        
        ChatMessage savedMessage = chatRepository.save(request.toChatMessage(chatroom, sender.getName()));
        
        return ChatMessageResponse.of(savedMessage, sender.getId());
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllRoom(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        List<MemberInChatRoom> memberInChatRooms = memberRepository.findAllMemberInChatRoomByMember(member);
        
        return ChatRoomResponse.fromList(memberInChatRooms);
    }
    
    @Transactional
    public void exitRoom(Long memberId, Long roomId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        ChatRoom chatroom = chatRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);
        
        memberRepository.deleteMemberInChatRoomByMemberAndChatRoom(member, chatroom);
        ChatRoom reducedChatRoom = chatroom.withReduceMemberCount();
        
        // 멤버가 남아있지 않다면 채팅방도 삭제
        if (reducedChatRoom.getMemberCount() == 0) {
            chatRepository.delete(reducedChatRoom);
        } else {
            chatRepository.save(reducedChatRoom);
        }
    }
}
