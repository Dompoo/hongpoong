package Dompoo.Hongpoong.domain.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessage {

    private final Long id;
    private final String message;
    private final String sender;
    private final ChatRoom chatRoom;
}
