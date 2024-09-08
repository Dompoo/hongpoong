package Dompoo.Hongpoong.api.dto.chat.request;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ChatRoomCreateRequest {
    
    @Schema(example = "걸궁 답사")
    private final String chatroomName;
    
    @Schema(example = "{1, 2, 3}")
    private final List<Long> memberIds;
    
    public ChatRoom toChatRoom() {
        return ChatRoom.builder()
                .roomName(chatroomName)
                .memberCount(memberIds.size())
                .build();
    }
}
