package Dompoo.Hongpoong.api.dto.auth.response;

import Dompoo.Hongpoong.domain.jpaEntity.SignUpJpaEntity;
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
    
    @Schema(example = "산틀")
    private final String club;
    
    @Schema(example = "19")
    private final Integer enrollmentNumber;
    
    public static SignUpResponse from(SignUpJpaEntity signUpJpaEntity) {
        return SignUpResponse.builder()
                .signupId(signUpJpaEntity.getId())
                .email(signUpJpaEntity.getEmail())
                .name(signUpJpaEntity.getName())
                .nickname(signUpJpaEntity.getNickname())
                .club(signUpJpaEntity.getClub().korName)
                .enrollmentNumber(signUpJpaEntity.getEnrollmentNumber())
                .build();
    }
}
