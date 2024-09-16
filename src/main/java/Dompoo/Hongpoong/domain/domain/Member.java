package Dompoo.Hongpoong.domain.domain;

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

    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String password;
    private final Role role;
    private final Club club;
    private final Integer enrollmentNumber;
    private final String profileImageUrl;
    private final Boolean pushAlarm;
    
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
    
    public Member withEdited(String name, String nickname, Club club, Integer enrollmentNumber, String profileImageUrl, String newPassword, PasswordEncoder encoder) {
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .name(name == null ? this.name : name)
                .nickname(nickname == null ? this.nickname : nickname)
                .password(newPassword == null ? this.password : encoder.encode(newPassword))
                .role(this.role)
                .club(club == null ? this.club : club)
                .enrollmentNumber(enrollmentNumber == null ? this.enrollmentNumber : enrollmentNumber)
                .profileImageUrl(profileImageUrl == null ? this.profileImageUrl : profileImageUrl)
                .pushAlarm(this.pushAlarm)
                .build();
    }
    
    public Member withEditedSetting(Boolean pushAlarm) {
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .nickname(this.nickname)
                .password(this.password)
                .role(this.role)
                .club(this.club)
                .enrollmentNumber(this.enrollmentNumber)
                .profileImageUrl(this.profileImageUrl)
                .pushAlarm(pushAlarm == null ? this.pushAlarm : pushAlarm)
                .build();
    }
    
    public Member withEditedRole(Role role) {
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .nickname(this.nickname)
                .password(this.password)
                .role(role == null ? this.role : role)
                .club(this.club)
                .enrollmentNumber(this.enrollmentNumber)
                .profileImageUrl(this.profileImageUrl)
                .pushAlarm(this.pushAlarm)
                .build();
    }
}
