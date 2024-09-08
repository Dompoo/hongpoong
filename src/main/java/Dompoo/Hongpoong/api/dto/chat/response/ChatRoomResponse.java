package Dompoo.Hongpoong.api.dto.chat.response;

import Dompoo.Hongpoong.domain.entity.ChatRoom;
import Dompoo.Hongpoong.domain.entity.MemberInChatRoom;
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
    private final Long id;
    
    @Schema(example = "걸궁 답사")
    private final String roomName;
    
    @Schema(example = "10")
    private final Integer memberCount;
    
    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .roomName(chatRoom.getRoomName())
                .memberCount(chatRoom.getMemberCount())
                .build();
    }
    
    public static List<ChatRoomResponse> fromList(List<MemberInChatRoom> memberInChatRooms) {
        return memberInChatRooms.stream()
                .map(memberInChatRoom -> ChatRoomResponse.of(memberInChatRoom.getChatRoom()))
                .toList();
    }
    
}
