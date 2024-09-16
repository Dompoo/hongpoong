package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.api.dto.info.request.InfoEditDto;
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
    
    public void edit(InfoEditDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
    }
}
