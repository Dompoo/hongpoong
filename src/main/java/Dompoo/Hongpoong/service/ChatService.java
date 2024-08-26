package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.api.dto.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.api.dto.response.chat.ChatMessageDTO;
import Dompoo.Hongpoong.api.dto.response.chat.ChatRoomResponse;
import Dompoo.Hongpoong.common.exception.impl.ChatRoomNotFound;
import Dompoo.Hongpoong.common.exception.impl.MemberNotFound;
import Dompoo.Hongpoong.domain.ChatMessage;
import Dompoo.Hongpoong.domain.ChatRoom;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.UserInChatRoom;
import Dompoo.Hongpoong.repository.ChatMessageRepository;
import Dompoo.Hongpoong.repository.ChatRoomRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.repository.UserInChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final UserInChatRoomRepository userInChatRoomRepository;
    
    public ChatRoomResponse createRoom(ChatRoomCreateRequest request){
        Stream<Member> members = request.getMembers().stream()
                .map(id -> memberRepository.findById(id)
                        .orElseThrow(MemberNotFound::new));

        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(request.getName())
                .build();
        
        List<UserInChatRoom> list = members.map(member -> new UserInChatRoom(member, chatRoom)).toList();
        
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        userInChatRoomRepository.saveAll(list);

        return new ChatRoomResponse(savedRoom);
    }

    //내가 참여한 채팅방 조회
    public List<ChatRoomResponse> findAllRoom(Long memberId){
        return chatRoomRepository.findAll().stream()
                .filter(chatRoom -> chatRoom.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(memberId)))
                .map(ChatRoomResponse::new)
                .toList();
    }


    public void exitRoom(Long memberId, Long roomId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFound::new);

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);

        //채팅방에 해당 회원이 존재한다면
        if (room.getMembers().stream().anyMatch(m -> m.getId().equals(memberId))) {
            if (room.getMembers().size() == 1) {
                //마지막 인원이면 채팅방 삭제
                chatRoomRepository.delete(room);
            } else {
                //마지막 인원이 아니면 회원만 삭제
                room.getMembers().remove(member);
                member.getChatRooms().remove(room);
            }
        }
    }

    public ChatMessageDTO createChat(Long roomId, ChatMessageDTO request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(ChatRoomNotFound::new);

        Member sender = memberRepository.findById(request.getSenderId())
                .orElseThrow(MemberNotFound::new);

        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(room)
                .message(request.getMessage())
                .sender(sender.getUsername())
                .build());

        return ChatMessageDTO.builder()
                .message(message.getMessage())
                .senderId(request.getSenderId())
                .build();
    }
}
