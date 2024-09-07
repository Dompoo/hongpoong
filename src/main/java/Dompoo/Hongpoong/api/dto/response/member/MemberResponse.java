package Dompoo.Hongpoong.api.dto.response.member;

import Dompoo.Hongpoong.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String club;
    private Integer enrollmentNumber;
    private String profileImageUrl;
    
    @Builder
    private MemberResponse(Long id, String email, String name, String nickname, String club, Integer enrollmentNumber, String profileImageUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.club = club;
        this.enrollmentNumber = enrollmentNumber;
        this.profileImageUrl = profileImageUrl;
    }
    
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .club(member.getClub().korName)
                .enrollmentNumber(member.getEnrollmentNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
