package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.ChatRoom;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String roomName;
    
    private Integer memberCount;
    
    public ChatRoom toDomain() {
        return ChatRoom.builder()
                .id(this.id)
                .roomName(this.roomName)
                .memberCount(this.memberCount)
                .build();
    }
    
    public static ChatRoomJpaEntity of(ChatRoom chatRoom) {
        return ChatRoomJpaEntity.builder()
                .id(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .memberCount(chatRoom.getMemberCount())
                .build();
    }
}
