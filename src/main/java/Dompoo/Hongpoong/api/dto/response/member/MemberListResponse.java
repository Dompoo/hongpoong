package Dompoo.Hongpoong.api.dto.response.member;

import Dompoo.Hongpoong.domain.Member;
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
public class MemberListResponse {
    private Long id;
    private String email;
    private String username;
    private String club;

    public MemberListResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.club = member.getClub();
    }
}
