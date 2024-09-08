package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberStatusResponse {
    
    @Schema(example = "1")
    private final Long memberId;
    
    @Schema(example = "email@gmail.com")
    private final String email;
    
    @Schema(example = "이창근")
    private final String name;
    
    @Schema(example = "불꽃남자")
    private final String nickname;
    
    @Schema(example = "산틀")
    private final String club;
    
    @Schema(example = "19")
    private final Integer enrollmentNumber;
    
    @Schema(example = "image.com/1")
    private final String profileImageUrl;
    
    @Schema(example = "true")
    private final Boolean push;
    
    public static MemberStatusResponse from(Member member) {
        return MemberStatusResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .club(member.getClub().korName)
                .enrollmentNumber(member.getEnrollmentNumber())
                .profileImageUrl(member.getProfileImageUrl())
                .push(member.isPushAlarm())
                .build();
    }
}
