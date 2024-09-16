package Dompoo.Hongpoong.domain.domain;

import Dompoo.Hongpoong.api.dto.common.request.SettingEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberEditDto;
import Dompoo.Hongpoong.api.dto.member.request.MemberRoleEditDto;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String password;
    private Role role;
    private Club club;
    private Integer enrollmentNumber;
    private String profileImageUrl;
    private Boolean pushAlarm;
    
    public static Member from(SignUp signUp) {
        return Member.builder()
                .email(signUp.getEmail())
                .name(signUp.getName())
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
