package Dompoo.Hongpoong.api.dto.chat.response;

import Dompoo.Hongpoong.domain.entity.ChatMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageResponse {

    private Long senderId;
    private String message;
    
    public static ChatMessageResponse of(ChatMessage message, Long senderId) {
        return ChatMessageResponse.builder()
                .senderId(senderId)
                .message(message.getMessage())
                .build();
    }
}
