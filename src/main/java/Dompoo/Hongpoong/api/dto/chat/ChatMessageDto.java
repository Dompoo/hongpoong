package Dompoo.Hongpoong.api.dto.chat;

import Dompoo.Hongpoong.domain.entity.ChatMessage;
import Dompoo.Hongpoong.domain.entity.ChatRoom;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {

    private Long senderId;
    private String message;

    @Builder
    private ChatMessageDto(Long senderId, String message) {
        this.senderId = senderId;
        this.message = message;
    }
    
    public ChatMessage toChatMessage(ChatRoom chatRoom, String sender) {
        return ChatMessage.builder()
                .sender(sender)
                .message(message)
                .chatRoom(chatRoom)
                .build();
    }
    
    public static ChatMessageDto of(ChatMessage message, Long senderId) {
        return ChatMessageDto.builder()
                .senderId(senderId)
                .message(message.getMessage())
                .build();
    }
}
