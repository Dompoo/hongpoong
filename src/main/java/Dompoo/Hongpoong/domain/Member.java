package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "responseMember", cascade = CascadeType.REMOVE)
    private List<Rental> giveRentals = new ArrayList<>();

    @OneToMany(mappedBy = "requestMember", cascade = CascadeType.REMOVE)
    private List<Rental> receiveRentals = new ArrayList<>();

    @ManyToMany
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @Builder
    public Member(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public void joinChatRoom(ChatRoom chatRoom){
        this.chatRooms.add(chatRoom);
        chatRoom.getMembers().add(this);
    }
}
