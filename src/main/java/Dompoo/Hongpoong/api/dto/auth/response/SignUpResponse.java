package Dompoo.Hongpoong.api.dto.auth.response;

import Dompoo.Hongpoong.domain.entity.SignUp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpResponse {
    
    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String password;
    private final String club;
    private final Integer enrollmentNumber;
    
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
