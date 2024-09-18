package Dompoo.Hongpoong.api.dto.chat.response;

import Dompoo.Hongpoong.domain.domain.ChatRoom;
import Dompoo.Hongpoong.domain.domain.MemberInChatRoom;
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
    
    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatroomId(chatRoom.getId())
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
