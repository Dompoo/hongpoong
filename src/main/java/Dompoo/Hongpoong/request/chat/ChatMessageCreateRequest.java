package Dompoo.Hongpoong.request.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageCreateRequest {
    private MessageType type;
    private Long roomId;
    private String sender;
    private String message;

    public enum MessageType {
        ENTER, TALK
    }
}
