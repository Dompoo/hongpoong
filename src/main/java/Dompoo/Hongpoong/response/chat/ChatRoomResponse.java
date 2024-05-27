package Dompoo.Hongpoong.response.chat;

import Dompoo.Hongpoong.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

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
    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomName = chatRoom.getRoomName();
        this.memberCount = chatRoom.getMembers().size();
    }
}
