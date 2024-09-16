package Dompoo.Hongpoong.domain.jpaEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String message;
    
    private String sender;
    
    @ManyToOne @JoinColumn(name = "room_id")
    private ChatRoomJpaEntity chatRoomJpaEntity;
}
