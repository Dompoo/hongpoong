package Dompoo.Hongpoong.service;

import Dompoo.Hongpoong.domain.ChatRoom;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.exception.MemberNotFound;
import Dompoo.Hongpoong.repository.ChatRoomRepository;
import Dompoo.Hongpoong.repository.MemberRepository;
import Dompoo.Hongpoong.request.chat.ChatRoomCreateRequest;
import Dompoo.Hongpoong.response.ChatRoomResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Data
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatRoomResponse createRoom(ChatRoomCreateRequest request){
        List<Member> members = request.getMembers().stream()
                .map(id -> memberRepository.findById(id)
                        .orElseThrow(MemberNotFound::new))
                .toList();

        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.getName())
                .members(members)
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponse(savedRoom);
    }

    public List<ChatRoomResponse> findAll(){
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResponse::new)
                .toList();
    }

//    public ChatRoomDetailResponse findOne(Long roomId){
//        ChatRoom chatRoom = repository.findById(roomId)
//                .orElseThrow(ChatRoomNotFound::new);
//
//        return new ChatRoomDetailResponse(chatRoom);
//    }
}
