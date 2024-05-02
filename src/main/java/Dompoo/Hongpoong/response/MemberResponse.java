package Dompoo.Hongpoong.response;

import Dompoo.Hongpoong.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String username;
    private String password;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.password = member.getPassword();
    }
}
