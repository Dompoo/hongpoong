//package Dompoo.Hongpoong.domain;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class ChatMessage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long chatId;
//    private String roomId;
//    private String sender;
//    private String message;
//
//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private ChatRoom chatRoom;
//
//    @Builder
//    public ChatMessage(String roomId, String sender, String message) {
//        this.roomId = roomId;
//        this.sender = sender;
//        this.message = message;
//    }
//}
