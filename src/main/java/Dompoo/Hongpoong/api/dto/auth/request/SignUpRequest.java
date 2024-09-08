package Dompoo.Hongpoong.api.dto.auth.request;

import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.enums.Club;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
public class SignUpRequest {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotNull(message = "이메일은 비어있을 수 없습니다.")
    private final String email;
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private final String name;
    @NotBlank(message = "패명은 비어있을 수 없습니다.")
    private final String nickname;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private final String password;
    @NotBlank(message = "동아리는 비어있을 수 없습니다.")
    private final String club;
    @NotNull(message = "학번은 비어있을 수 없습니다.")
    private final Integer enrollmentNumber;
    
    public SignUp toSignUp(PasswordEncoder encoder) {
        return SignUp.builder()
                .email(email)
                .name(name)
                .password(encoder.encode(password))
                .club(Club.from(club))
                .enrollmentNumber(enrollmentNumber)
                .build();
    }
}
