package Dompoo.Hongpoong.domain.entity;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditDto;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static Dompoo.Hongpoong.domain.enums.Role.USER;

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
    private String profileImageUrl;
    private boolean pushAlarm;
    
    @Builder
    private Member(String email, String name, String nickname, String password, Role role, Club club, Integer enrollmentNumber, String profileImageUrl, boolean pushAlarm) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.club = club;
        this.enrollmentNumber = enrollmentNumber;
        this.profileImageUrl = profileImageUrl;
        this.pushAlarm = pushAlarm;
    }
    
    public static Member from(SignUp signUp) {
        return Member.builder()
                .email(signUp.getEmail())
                .name(signUp.getPassword())
                .nickname(signUp.getNickname())
                .password(signUp.getPassword())
                .role(USER)
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
