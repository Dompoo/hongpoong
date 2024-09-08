package Dompoo.Hongpoong.api.dto.auth.response;

import Dompoo.Hongpoong.domain.entity.SignUp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpResponse {
    
    @Schema(example = "1")
    private final Long signupId;
    
    @Schema(example = "email@gmail.com")
    private final String email;
    
    @Schema(example = "이창근")
    private final String name;
    
    @Schema(example = "불꽃남자")
    private final String nickname;
    
    @Schema(example = "0912md0m10102")
    private final String password;
    
    @Schema(example = "산틀")
    private final String club;
    
    @Schema(example = "19")
    private final Integer enrollmentNumber;
    
    public static SignUpResponse from(SignUp signUp) {
        return SignUpResponse.builder()
                .signupId(signUp.getId())
                .email(signUp.getEmail())
                .name(signUp.getName())
                .nickname(signUp.getNickname())
                .password(signUp.getPassword())
                .club(signUp.getClub().korName)
                .enrollmentNumber(signUp.getEnrollmentNumber())
                .build();
    }
}
