package Dompoo.Hongpoong.domain;

import Dompoo.Hongpoong.api.dto.request.member.MemberEditDto;
import Dompoo.Hongpoong.common.exception.impl.PasswordNotSame;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static Dompoo.Hongpoong.domain.enums.Role.ROLE_USER;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private Club club;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Setting setting;

    @Builder
    public Member(String email, String username, String password, Club club) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.club = club;
        this.role = ROLE_USER;
    }

    public Member(SignUp signUp) {
        this.email = signUp.getEmail();
        this.username = signUp.getEmail();
        this.password = signUp.getPassword();
        this.club = signUp.getClub();
        this.role = ROLE_USER;
    }
    
    public void edit(MemberEditDto dto, PasswordEncoder encoder) {
        if (dto.isPasswordSame()) {
            this.password = encoder.encode(dto.getPassword1());
        } else {
            throw new PasswordNotSame();
        }
        
        if (dto.getUsername() != null) this.username = dto.getUsername();
    }
}
