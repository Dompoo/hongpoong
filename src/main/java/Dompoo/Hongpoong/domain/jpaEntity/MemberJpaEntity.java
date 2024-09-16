package Dompoo.Hongpoong.domain.jpaEntity;

import Dompoo.Hongpoong.domain.domain.Member;
import Dompoo.Hongpoong.domain.enums.Club;
import Dompoo.Hongpoong.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberJpaEntity {

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
    
    private Boolean pushAlarm;
    
    public Member toDomain() {
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
                .pushAlarm(this.pushAlarm)
                .build();
    }
    
    public static MemberJpaEntity of(Member member) {
        return MemberJpaEntity.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .role(member.getRole())
                .club(member.getClub())
                .enrollmentNumber(member.getEnrollmentNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .pushAlarm(member.getPushAlarm())
                .build();
    }
    
    public static List<MemberJpaEntity> of(List<Member> members) {
        return members.stream()
                .map(MemberJpaEntity::of)
                .toList();
    }
}
