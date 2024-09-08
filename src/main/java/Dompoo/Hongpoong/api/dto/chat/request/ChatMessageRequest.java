package Dompoo.Hongpoong.api.dto.chat.request;

import Dompoo.Hongpoong.domain.entity.ChatMessage;
import Dompoo.Hongpoong.domain.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class ChatMessageRequest {
    
    @Schema(example = "1")
    private Long senderId;
    
    @Schema(example = "하이요")
    private String message;
    
    public ChatMessage toChatMessage(ChatRoom chatRoom, String sender) {
        return ChatMessage.builder()
                .sender(sender)
                .message(message)
                .chatRoom(chatRoom)
                .build();
    }
}
