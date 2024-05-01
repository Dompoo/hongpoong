package Dompoo.Hongpoong.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Builder
    public Info(String title, String content, LocalDateTime date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
