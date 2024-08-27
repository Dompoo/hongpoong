package Dompoo.Hongpoong.api.dto.request.chat;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
/*
RequestBody
{
    "name":"roomName"
    "members":[1, 2, 3]
}
 */
public class ChatRoomCreateRequest {

    private String name;
    private List<Long> members;

    @Builder
    private ChatRoomCreateRequest(String name, List<Long> members) {
        this.name = name;
        this.members = members;
    }
    
    public ChatRoom toChatRoom() {
        return ChatRoom.builder()
                .roomName(name)
                .memberCount(members.size())
                .build();
    }
}
