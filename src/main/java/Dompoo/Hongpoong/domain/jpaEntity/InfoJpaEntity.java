package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.Info;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InfoJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private String content;
    
    private LocalDateTime date;
    
    @ManyToOne @JoinColumn(name = "member_id")
    private MemberJpaEntity memberJpaEntity;
    
    public Info toDomain() {
        return Info.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .date(this.date)
                .member(this.memberJpaEntity.toDomain())
                .build();
    }
    
    public static InfoJpaEntity of(Info info) {
        return InfoJpaEntity.builder()
                .id(info.getId())
                .title(info.getTitle())
                .content(info.getContent())
                .date(info.getDate())
                .memberJpaEntity(MemberJpaEntity.of(info.getMember()))
                .build();
    }
}
