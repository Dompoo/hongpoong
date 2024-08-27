package Dompoo.Hongpoong.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    private int memberCount;
    
    @Builder
    private ChatRoom(String roomName, int memberCount) {
        this.roomName = roomName;
        this.memberCount = memberCount;
    }
    
    public void reduceMemberCount() {
        this.memberCount--;
    }
}
