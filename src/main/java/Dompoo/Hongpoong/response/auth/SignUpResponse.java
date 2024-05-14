package Dompoo.Hongpoong.response.auth;

import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.domain.SignUp;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody
{
    "email": "dompoo@gmail.com"
    "username": "dompoo"
    "password": "1234"
    "club": "SANTLE"
}
 */
public class SignUpResponse {

    private Long id;
    private String email;
    private String username;
    private String password;
    private Member.Club club;

    @Builder
    public SignUpResponse(SignUp signUp) {
        this.id = signUp.getId();
        this.email = signUp.getEmail();
        this.username = signUp.getUsername();
        this.password = signUp.getPassword();
        this.club = signUp.getClub();
    }
}
