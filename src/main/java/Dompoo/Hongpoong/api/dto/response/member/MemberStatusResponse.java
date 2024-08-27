package Dompoo.Hongpoong.api.dto.response.member;

import Dompoo.Hongpoong.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody

<단건조회시>
{
    "id": 1,
    "email": "admin",
    "username": "admin",
    "password": "$2a$10$fUgd2TdgIoz72Dn0I3tQg.SqRpUBLmDF6ukCcuj4bb9PYNjJ5l8te",
    "club": "산틀"
}
 */
public class MemberStatusResponse {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String club;
    
    @Builder
    private MemberStatusResponse(Long id, String email, String username, String password, String club) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.club = club;
    }
    
    public static MemberStatusResponse from(Member member) {
        return MemberStatusResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .password(member.getPassword())
                .club(member.getClub().korName)
                .build();
    }
}
