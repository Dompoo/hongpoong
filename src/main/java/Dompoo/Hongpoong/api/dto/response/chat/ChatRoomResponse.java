package Dompoo.Hongpoong.api.dto.response.chat;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
/*
ResponseBody

<List조회시>
[
    {
        "id": 1,
        "roomName": "채팅방1",
        "memberCount": 3
    },
    . . .
]
 */
public class ChatRoomResponse {

    private Long id; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private Integer memberCount; // 채팅방 인원 수
    
    @Builder
    private ChatRoomResponse(Long id, String roomName, Integer memberCount) {
        this.id = id;
        this.roomName = roomName;
        this.memberCount = memberCount;
    }
    
    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .memberCount(chatRoom.getMemberCount())
                .build();
    }
    
    public static List<ChatRoomResponse> fromList(List<MemberInChatRoom> memberInChatRooms) {
        return memberInChatRooms.stream()
                .map(memberInChatRoom -> ChatRoomResponse.of(memberInChatRoom.getChatRoom()))
                .toList();
    }
    
}
