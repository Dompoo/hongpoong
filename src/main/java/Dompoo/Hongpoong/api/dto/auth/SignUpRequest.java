package Dompoo.Hongpoong.api.dto.auth;

import Dompoo.Hongpoong.domain.entity.SignUp;
import Dompoo.Hongpoong.domain.enums.Club;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotNull(message = "이메일은 비어있을 수 없습니다.")
    private String email;
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String name;
    @NotBlank(message = "패명은 비어있을 수 없습니다.")
    private String nickname;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;
    @NotBlank(message = "동아리는 비어있을 수 없습니다.")
    private String club;
    @NotNull(message = "학번은 비어있을 수 없습니다.")
    private Integer enrollmentNumber;
    
    @Builder
    private SignUpRequest(String email, String name, String nickname, String password, String club, Integer enrollmentNumber) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.club = club;
        this.enrollmentNumber = enrollmentNumber;
    }
    
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
