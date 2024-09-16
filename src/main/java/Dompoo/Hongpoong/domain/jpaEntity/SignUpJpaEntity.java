package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.SignUp;
import Dompoo.Hongpoong.domain.enums.Club;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    
    private String name;
    
    private String nickname;
    
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Club club;
    
    private Integer enrollmentNumber;
    
    public SignUp toDomain() {
        return SignUp.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .nickname(this.nickname)
                .password(this.password)
                .club(this.club)
                .enrollmentNumber(this.enrollmentNumber)
                .build();
    }
    
    public static SignUpJpaEntity of(SignUp signUp) {
        return SignUpJpaEntity.builder()
                .id(signUp.getId())
                .email(signUp.getEmail())
                .name(signUp.getName())
                .nickname(signUp.getNickname())
                .password(signUp.getPassword())
                .club(signUp.getClub())
                .enrollmentNumber(signUp.getEnrollmentNumber())
                .build();
    }
}
