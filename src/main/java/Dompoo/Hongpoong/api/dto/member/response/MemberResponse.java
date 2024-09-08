package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.entity.Member;
import Dompoo.Hongpoong.domain.entity.ReservationParticipate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    
    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String club;
    private final Integer enrollmentNumber;
    private final String profileImageUrl;
    
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
    
    public static List<MemberResponse> fromList(List<Member> members) {
        return members.stream().map(MemberResponse::from).toList();
    }
    
    public static List<MemberResponse> fromParticipates(List<ReservationParticipate> participateList) {
        return participateList.stream().map(participate -> MemberResponse.from(participate.getMember())).toList();
    }
}
