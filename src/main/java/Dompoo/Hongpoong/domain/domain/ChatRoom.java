package Dompoo.Hongpoong.domain.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {

    private final Long id;
    private final String roomName;
    private final Integer memberCount;
    
    public ChatRoom withReduceMemberCount() {
        return ChatRoom.builder()
                .id(this.id)
                .roomName(this.roomName)
                .memberCount(this.memberCount - 1)
                .build();
    }
}
