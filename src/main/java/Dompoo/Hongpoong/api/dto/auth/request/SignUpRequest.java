package Dompoo.Hongpoong.api.dto.auth.request;

import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.enums.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class SignUpRequest {
    
    @NotNull(message = "이메일은 비어있을 수 없습니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    @Schema(example = "email@gmail.com")
    private final String email;
    
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    @Schema(example = "이창근")
    private final String name;
    
    @Schema(example = "불꽃남자")
    private final String nickname;
    
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    @Schema(example = "1234")
    private final String password;
    
    @NotNull(message = "동아리는 비어있을 수 없습니다.")
    @Schema(enumAsRef = true)
    private final Club club;
    
    @NotNull(message = "학번은 비어있을 수 없습니다.")
    @Schema(example = "19")
    private final Integer enrollmentNumber;
    
    public SignUp toSignUp(PasswordEncoder encoder) {
        return SignUp.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(encoder.encode(password))
                .club(club)
                .enrollmentNumber(enrollmentNumber)
                .build();
    }
}
