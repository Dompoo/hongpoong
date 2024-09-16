package Dompoo.Hongpoong.api.dto.chat.response;

import Dompoo.Hongpoong.domain.jpaEntity.ChatRoomJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberInChatRoomJpaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomResponse {
    
    @Schema(example = "1")
    private final Long chatroomId;
    
    @Schema(example = "걸궁 답사")
    private final String roomName;
    
    @Schema(example = "10")
    private final Integer memberCount;
    
    public static ChatRoomResponse of(ChatRoomJpaEntity chatRoomJpaEntity) {
        return ChatRoomResponse.builder()
                .chatroomId(chatRoomJpaEntity.getId())
                .roomName(chatRoomJpaEntity.getRoomName())
                .memberCount(chatRoomJpaEntity.getMemberCount())
                .build();
    }
    
    public static List<ChatRoomResponse> fromList(List<MemberInChatRoomJpaEntity> memberInChatRoomJpaEntities) {
        return memberInChatRoomJpaEntities.stream()
                .map(memberInChatRoom -> ChatRoomResponse.of(memberInChatRoom.getChatRoomJpaEntity()))
                .toList();
    }
    
}
