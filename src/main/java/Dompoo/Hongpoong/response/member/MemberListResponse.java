package Dompoo.Hongpoong.response.member;

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
    },
    {
        "id": 2,
        "email": "dompoo@gmail.com",
        "username": "dompoo",
    },
    . . .
]
 */
public class MemberListResponse {
    private Long id;
    private String email;
    private String username;

    public MemberListResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
    }
}
