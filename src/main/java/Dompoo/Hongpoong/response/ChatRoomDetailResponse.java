//package Dompoo.Hongpoong.response;
//
//import Dompoo.Hongpoong.domain.ChatMessage;
//import Dompoo.Hongpoong.domain.ChatRoom;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.List;
//
//@Getter
//public class ChatRoomDetailResponse {
//
//    private Long roomId; // 채팅방 아이디
//    private String name; // 채팅방 이름
//    private List<ChatMessage> messages;
//
//    @Builder
//    public ChatRoomDetailResponse(ChatRoom chatRoom) {
//        this.roomId = chatRoom.getRoomId();
//        this.name = chatRoom.getName();
//        this.messages = chatRoom.getMessages();
//    }
//}
