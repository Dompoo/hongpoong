package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.info.request.InfoEditDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    @Builder
    public Info(String title, String content, LocalDateTime date, Member member) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.member = member;
    }
    
    public void edit(InfoEditDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
    }
}
