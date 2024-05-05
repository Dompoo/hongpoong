package Dompoo.Hongpoong.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Whitelist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean isAccepted;
    private Boolean isUsed;

    @Builder
    public Whitelist(String email, Boolean isAccepted) {
        this.email = email;
        this.isAccepted = isAccepted;
        this.isUsed = false;
    }
}
