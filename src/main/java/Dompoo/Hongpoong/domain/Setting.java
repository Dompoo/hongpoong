package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean push;

    @Builder
    public Setting(Member member) {
        setMember(member);
        this.push = false;
    }

    public void setMember(Member member) {
        member.setSetting(this);
        this.member = member;
    }
}
