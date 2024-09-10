package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.domain.enums.Club;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    
    private String name;
    
    private String nickname;
    
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Club club;
    
    private Integer enrollmentNumber;
}
