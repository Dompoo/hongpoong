package Dompoo.Hongpoong.domain;

import Dompoo.Hongpoong.domain.Member.Club;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private Club club;

    @Builder
    public SignUp(String email, String username, String password, Club club) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.club = club;
    }
}
