package Dompoo.Hongpoong.api.dto.response.auth;

import Dompoo.Hongpoong.domain.entity.SignUp;
import lombok.Builder;
import lombok.Getter;

@Getter
/*
ResponseBody
{
    "email": "dompoo@gmail.com"
    "username": "dompoo"
    "password": "1234"
    "club": "산틀"
}
 */
public class SignUpResponse {

    private Long id;
    private String email;
    private String username;
    private String password;
    private String club;
    
    @Builder
    private SignUpResponse(Long id, String email, String username, String password, String club) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.club = club;
    }
    
    public static SignUpResponse from(SignUp signUp) {
        return SignUpResponse.builder()
                .id(signUp.getId())
                .email(signUp.getEmail())
                .username(signUp.getUsername())
                .password(signUp.getPassword())
                .club(signUp.getClub().korName)
                .build();
    }
}
