package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
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
    
    @Schema(example = "패짱")
    private final String role;
    
    @Schema(example = "image.com/1")
    private final String profileImageUrl;
    
    @Schema(example = "true")
    private final Boolean push;
    
    public static MemberStatusResponse from(MemberJpaEntity memberJpaEntity) {
        return MemberStatusResponse.builder()
                .memberId(memberJpaEntity.getId())
                .email(memberJpaEntity.getEmail())
                .name(memberJpaEntity.getName())
                .nickname(memberJpaEntity.getNickname())
                .club(memberJpaEntity.getClub().korName)
                .enrollmentNumber(memberJpaEntity.getEnrollmentNumber())
                .role(memberJpaEntity.getRole().korName)
                .profileImageUrl(memberJpaEntity.getProfileImageUrl())
                .push(memberJpaEntity.getPushAlarm())
                .build();
    }
}
