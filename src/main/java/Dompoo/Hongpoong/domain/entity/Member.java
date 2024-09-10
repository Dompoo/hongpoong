package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditDto;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    
    private String name;
    
    private String nickname;
    
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Enumerated(EnumType.STRING)
    private Club club;
    
    private Integer enrollmentNumber;
    
    private String profileImageUrl;
    
    private boolean pushAlarm;
    
    public static Member from(SignUp signUp) {
        return Member.builder()
                .email(signUp.getEmail())
                .name(signUp.getPassword())
                .nickname(signUp.getNickname())
                .password(signUp.getPassword())
                .role(Role.MEMBER)
                .club(signUp.getClub())
                .enrollmentNumber(signUp.getEnrollmentNumber())
                .pushAlarm(false)
                .build();
    }
    
    public void edit(MemberEditDto dto, PasswordEncoder encoder) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getNickname() != null) this.nickname = dto.getNickname();
        if (dto.getClub() != null) this.club = dto.getClub();
        if (dto.getEnrollmentNumber() != null) this.enrollmentNumber = dto.getEnrollmentNumber();
        if (dto.getProfileImageUrl() != null) this.profileImageUrl = dto.getProfileImageUrl();
        if (dto.getNewPassword() != null) this.password = encoder.encode(dto.getNewPassword());
    }
    
    public void editSetting(SettingEditDto dto) {
        if (dto.getPush() != null) this.pushAlarm = dto.getPush();
    }
    
    public void editRole(MemberRoleEditDto dto) {
        if (dto.getRole() != null) this.role = dto.getRole();
    }
}
