package Dompoo.Hongpoong.domain.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Info {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime date;
    private final Member member;
    
    public Info withEdited(String title, String content) {
        return Info.builder()
                .id(this.id)
                .title(title)
                .content(content)
                .date(this.date)
                .member(this.member)
                .build();
    }
}
