package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.jpaEntity.AttendanceJpaEntity;
import Dompoo.Hongpoong.domain.jpaEntity.MemberJpaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    
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
    
    public static MemberResponse from(MemberJpaEntity memberJpaEntity) {
        return MemberResponse.builder()
                .memberId(memberJpaEntity.getId())
                .email(memberJpaEntity.getEmail())
                .name(memberJpaEntity.getName())
                .nickname(memberJpaEntity.getNickname())
                .club(memberJpaEntity.getClub().korName)
                .enrollmentNumber(memberJpaEntity.getEnrollmentNumber())
                .role(memberJpaEntity.getRole().korName)
                .profileImageUrl(memberJpaEntity.getProfileImageUrl())
                .build();
    }
    
    public static List<MemberResponse> fromList(List<MemberJpaEntity> memberJpaEntities) {
        return memberJpaEntities.stream().map(MemberResponse::from).toList();
    }
    
    public static List<MemberResponse> fromParticipates(List<AttendanceJpaEntity> participateList) {
        return participateList.stream().map(participate -> MemberResponse.from(participate.getMemberJpaEntity())).toList();
    }
}
