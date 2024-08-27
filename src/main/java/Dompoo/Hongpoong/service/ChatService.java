package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.response.chat.ChatMessageDto;
import Dompoo.Hongpoong.api.dto.response.chat.ChatRoomResponse;
import Dompoo.Hongpoong.common.exception.impl.ChatRoomNotFound;
import Dompoo.Hongpoong.common.exception.impl.DeleteFailException;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.ChatMessage;
import Dompoo.Hongpoong.domain.ChatRoom;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.MemberInChatRoom;
import Dompoo.Hongpoong.repository.ChatMessageRepository;
import Dompoo.Hongpoong.repository.ChatRoomRepository;
import Dompoo.Hongpoong.repository.MemberInChatRoomRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final MemberInChatRoomRepository memberInChatRoomRepository;
    
    @Transactional
    public ChatRoomResponse createRoom(ChatRoomCreateRequest request) {
        List<Member> members = memberRepository.findAllByIdIn(request.getMembers());
        if (members.size() != request.getMembers().size()) throw new MemberNotFound();
        
        ChatRoom savedRoom = chatRoomRepository.save(request.toChatRoom());
        
        memberInChatRoomRepository.saveAll(members.stream()
                .map(member -> new MemberInChatRoom(member, savedRoom))
                .toList());
        
        return ChatRoomResponse.of(savedRoom);
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllRoom(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        List<MemberInChatRoom> memberInChatRooms = memberInChatRoomRepository.findAllByMember(member);
        
        return ChatRoomResponse.fromList(memberInChatRooms);
    }

    @Transactional
    public void exitRoom(Long memberId, Long roomId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);
        
        ChatRoom chatroom = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);
        
        // 해당 멤버가 해당 채팅방에 있지 않다면 예외 발생
        if (!memberInChatRoomRepository.existsByMemberAndChatRoom(member, chatroom)) throw new DeleteFailException();
        
        memberInChatRoomRepository.deleteByChatRoom(chatroom);
        chatroom.reduceMemberCount();
        
        // 멤버가 남아있지 않다면 채팅방도 삭제
        if (chatroom.getMemberCount() == 0) {
            chatRoomRepository.delete(chatroom);
        }
    }
    
    @Transactional
    public ChatMessageDto createChat(Long roomId, ChatMessageDto request) {
        ChatRoom chatroom = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);

        Member sender = memberRepository.findById(request.getSenderId())
                .orElseThrow(MemberNotFound::new);

        ChatMessage savedMessage = chatMessageRepository.save(request.toChatMessage(chatroom, sender.getUsername()));
        
        return ChatMessageDto.of(savedMessage, sender.getId());
    }
}
