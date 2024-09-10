package Dompoo.Hongpoong.api.dto.attendance;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceResponse {
    
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
    private final Boolean isAttended;
    
    public static AttendanceResponse from(Member member, Boolean isAttended) {
        return AttendanceResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .club(member.getClub().korName)
                .enrollmentNumber(member.getEnrollmentNumber())
                .club(member.getClub().korName)
                .profileImageUrl(member.getProfileImageUrl())
                .isAttended(isAttended)
                .build();
    }
    
    public static List<AttendanceResponse> fromList(List<ReservationParticipate> participateList) {
        return participateList.stream().map(participate -> AttendanceResponse.from(participate.getMember(), participate.getAttend())).toList();
    }
}
