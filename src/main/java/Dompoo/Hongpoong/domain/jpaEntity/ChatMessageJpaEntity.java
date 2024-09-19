package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.ChatMessage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String message;
    
    private String sender;
    
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "room_id")
    private ChatRoomJpaEntity chatRoomJpaEntity;
    
    public ChatMessage toDomain() {
        return ChatMessage.builder()
                .id(this.id)
                .message(this.message)
                .sender(this.sender)
                .chatRoom(this.chatRoomJpaEntity.toDomain())
                .build();
    }
    
    public static ChatMessageJpaEntity of(ChatMessage chatMessage) {
        return ChatMessageJpaEntity.builder()
                .id(chatMessage.getId())
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender())
                .chatRoomJpaEntity(ChatRoomJpaEntity.of(chatMessage.getChatRoom()))
                .build();
    }
}
