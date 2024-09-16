package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.domain.Attendance;
import Dompoo.Hongpoong.domain.domain.Member;
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
    
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .club(member.getClub().korName)
                .enrollmentNumber(member.getEnrollmentNumber())
                .role(member.getRole().korName)
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
    
    public static List<MemberResponse> fromList(List<Member> members) {
        return members.stream().map(MemberResponse::from).toList();
    }
    
    public static List<MemberResponse> fromAttendances(List<Attendance> attendances) {
        return attendances.stream().map(attendance -> MemberResponse.from(attendance.getMember())).toList();
    }
}
