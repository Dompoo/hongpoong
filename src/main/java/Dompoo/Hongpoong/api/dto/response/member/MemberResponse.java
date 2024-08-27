package Dompoo.Hongpoong.api.dto.response.member;

import Dompoo.Hongpoong.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody

<List조회시>
[
    {
        "id": 1,
        "email": "admin",
        "username": "admin",
        "club": "산틀"
    },
    {
        "id": 2,
        "email": "dompoo@gmail.com",
        "username": "dompoo",
        "club": "산틀"
    },
    . . .
]
 */
public class MemberResponse {
    private Long id;
    private String email;
    private String username;
    private String club;
    
    @Builder
    private MemberResponse(Long id, String email, String username, String club) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.club = club;
    }
    
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .club(member.getClub().korName)
                .build();
    }
}
