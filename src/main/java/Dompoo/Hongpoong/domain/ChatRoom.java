package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId; // 채팅방 아이디
    private String name; // 채팅방 이름

    @ManyToMany
    @JoinTable(
            name = "chat_room_member",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members = new ArrayList<>();

//    @OneToMany(mappedBy = "chatRoom")
//    private List<ChatMessage> messages = new ArrayList<>();

    @Builder
    public ChatRoom(Long roomId, String name, List<Member> members){
        this.roomId = roomId;
        this.name = name;
        members.forEach(this::addMember);
    }

    public void addMember(Member member){
        this.members.add(member);
        member.getChatRooms().add(this);
    }
}
