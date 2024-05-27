package Dompoo.Hongpoong.response.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDTO {

    private Long senderId;
    private String message;

    @Builder
    public ChatMessageDTO(Long senderId, String message) {
        this.senderId = senderId;
        this.message = message;
    }
}
