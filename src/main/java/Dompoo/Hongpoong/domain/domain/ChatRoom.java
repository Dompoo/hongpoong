package Dompoo.Hongpoong.domain.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {

    private Long id;
    private String roomName;
    private Integer memberCount;
    
    public void reduceMemberCount() {
        this.memberCount--;
    }
}
