package Dompoo.Hongpoong.response.member;

import Dompoo.Hongpoong.domain.Member;
import lombok.Getter;

@Getter
/*
ResponseBody

<단건조회시>
{
    "id": 1,
    "email": "admin",
    "username": "admin",
    "password": "$2a$10$fUgd2TdgIoz72Dn0I3tQg.SqRpUBLmDF6ukCcuj4bb9PYNjJ5l8te"
}
 */
public class MemberStatusResponse {
    private Long id;
    private String email;
    private String username;
    private String password;

    public MemberStatusResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.password = member.getPassword();
    }
}
