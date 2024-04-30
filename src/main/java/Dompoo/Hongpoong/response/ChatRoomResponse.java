package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomResponse {

    private Long roomId; // 채팅방 아이디
    private String name; // 채팅방 이름
    private Integer memberCount; // 채팅방 인원 수

    @Builder
    public ChatRoomResponse(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.name = chatRoom.getName();
        this.memberCount = chatRoom.getMembers().size();
    }
}
