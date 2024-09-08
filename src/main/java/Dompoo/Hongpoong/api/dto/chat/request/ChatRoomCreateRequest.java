package Dompoo.Hongpoong.api.dto.chat.request;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ChatRoomCreateRequest {

    private final String name;
    private final List<Long> members;
    
    public ChatRoom toChatRoom() {
        return ChatRoom.builder()
                .roomName(name)
                .memberCount(members.size())
                .build();
    }
}
