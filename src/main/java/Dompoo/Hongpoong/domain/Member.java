package Dompoo.Hongpoong.domain;

import Dompoo.Hongpoong.api.dto.request.common.SettingSaveDto;
import Dompoo.Hongpoong.api.dto.request.member.MemberEditDto;
import Dompoo.Hongpoong.common.exception.impl.PasswordNotSame;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private boolean pushAlarm;

    @Builder
    public Member(String email, String username, String password, Club club) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.club = club;
        this.pushAlarm = true;
        this.role = ROLE_USER;
    }

    public static Member from(SignUp signUp) {
        return Member.builder()
                .email(signUp.getEmail())
                .username(signUp.getEmail())
                .password(signUp.getPassword())
                .club(signUp.getClub())
                .build();
    }
    
    public void edit(MemberEditDto dto, PasswordEncoder encoder) {
        if (dto.isPasswordSame()) {
            this.password = encoder.encode(dto.getPassword1());
        } else {
            throw new PasswordNotSame();
        }
        
        if (dto.getUsername() != null) this.username = dto.getUsername();
    }
    
    public void editSetting(SettingSaveDto dto) {
        if (dto.getPush() != null) this.pushAlarm = dto.getPush();
    }
}
