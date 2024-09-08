package Dompoo.Hongpoong.api.dto.member.response;

import Dompoo.Hongpoong.domain.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberStatusResponse {
    
    private final Long id;
    private final String email;
    private final String username;
    private final String password;
    private final String club;
    
    public static MemberStatusResponse from(Member member) {
        return MemberStatusResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getName())
                .password(member.getPassword())
                .club(member.getClub().korName)
                .build();
    }
}
