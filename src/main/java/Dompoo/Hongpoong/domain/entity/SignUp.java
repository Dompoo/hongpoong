package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.domain.enums.Club;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String name;
    private String nickname;
    private String password;
    private Club club;
    private Integer enrollmentNumber;
    
    @Builder
    public SignUp(String email, String name, String nickname, String password, Club club, Integer enrollmentNumber) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.club = club;
        this.enrollmentNumber = enrollmentNumber;
    }
}
