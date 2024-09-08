package Dompoo.Hongpoong.api.dto.chat.request;

import Dompoo.Hongpoong.domain.entity.ChatMessage;
import Dompoo.Hongpoong.domain.entity.ChatRoom;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ChatMessageRequest {

    private Long senderId;
    private String message;
    
    public ChatMessage toChatMessage(ChatRoom chatRoom, String sender) {
        return ChatMessage.builder()
                .sender(sender)
                .message(message)
                .chatRoom(chatRoom)
                .build();
    }
}
