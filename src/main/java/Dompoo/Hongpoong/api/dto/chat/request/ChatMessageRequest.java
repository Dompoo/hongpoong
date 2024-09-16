package Dompoo.Hongpoong.api.dto.chat.request;

import Dompoo.Hongpoong.domain.jpaEntity.ChatMessageJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
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
    
    public ChatMessageJpaEntity toChatMessage(ChatRoomJpaEntity chatRoomJpaEntity, String sender) {
        return ChatMessageJpaEntity.builder()
                .sender(sender)
                .message(message)
                .chatRoomJpaEntity(chatRoomJpaEntity)
                .build();
    }
}
