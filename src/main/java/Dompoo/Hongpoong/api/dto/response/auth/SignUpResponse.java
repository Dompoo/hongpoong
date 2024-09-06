package Dompoo.Hongpoong.api.dto.response.auth;

import Dompoo.Hongpoong.domain.entity.SignUp;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponse {
    
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String password;
    private String club;
    private Integer enrollmentNumber;
    
    @Builder
    private SignUpResponse(Long id, String email, String name, String nickname, String password, String club, Integer enrollmentNumber) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.club = club;
        this.enrollmentNumber = enrollmentNumber;
    }
    
    public static SignUpResponse from(SignUp signUp) {
        return SignUpResponse.builder()
                .id(signUp.getId())
                .email(signUp.getEmail())
                .name(signUp.getName())
                .nickname(signUp.getNickname())
                .password(signUp.getPassword())
                .club(signUp.getClub().korName)
                .enrollmentNumber(signUp.getEnrollmentNumber())
                .build();
    }
}
