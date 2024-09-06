package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.request.common.SettingSaveDto;
import Dompoo.Hongpoong.api.dto.request.member.MemberEditDto;
import Dompoo.Hongpoong.common.exception.impl.PasswordNotSame;
import Dompoo.Hongpoong.common.security.SecurePolicy;
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
    private String name;
    private String nickname;
    private String password;
    private Role role;
    private Club club;
    private Integer enrollmentNumber;
    private boolean pushAlarm;
    
    @Builder
    private Member(String email, String name, String nickname, String password, Role role, Club club, Integer enrollmentNumber, boolean pushAlarm) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.club = club;
        this.enrollmentNumber = enrollmentNumber;
        this.pushAlarm = pushAlarm;
    }
    
    public static Member from(SignUp signUp) {
        return Member.builder()
                .email(signUp.getEmail())
                .name(signUp.getPassword())
                .nickname(signUp.getNickname())
                .password(signUp.getPassword())
                .role(ROLE_USER)
                .club(signUp.getClub())
                .enrollmentNumber(signUp.getEnrollmentNumber())
                .pushAlarm(false)
                .build();
    }
    
    public void edit(MemberEditDto dto, PasswordEncoder encoder) {
        if (dto.isPasswordSame()) {
            this.password = encoder.encode(dto.getPassword1());
        } else {
            throw new PasswordNotSame();
        }
        
        if (dto.getUsername() != null) this.name = dto.getUsername();
    }
    
    public void editSetting(SettingSaveDto dto) {
        if (dto.getPush() != null) this.pushAlarm = dto.getPush();
    }
    
    public boolean hasAccessLevel(SecurePolicy policy) {
        int accessLevel = this.getRole().getAccessLevel();
        
        return switch (policy) {
            case ALL_MEMBER -> accessLevel > 0;
            case ADMIN_AND_LEADER -> accessLevel > 1;
            case ADMIN_ONLY -> accessLevel > 2;
        };
    }
}
