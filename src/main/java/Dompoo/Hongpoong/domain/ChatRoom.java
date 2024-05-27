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
    private Long id;
    private String roomName;
    @OneToMany
    private List<ChatMessage> messages = new ArrayList<>();
    @ManyToMany
    private List<Member> members = new ArrayList<>();

    @Builder
    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }

    public void addMember(Member member){
        this.members.add(member);
        member.getChatRooms().add(this);
    }
}
