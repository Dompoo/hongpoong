package Dompoo.Hongpoong.api.dto.request.auth;

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
/*
RequestBody
{
    "email" : "dompoo@gmail.com",
    "username" : "창근",
    "password1" : "1234",
    "password2" : "1234"
    "club" : 0
}
 */
public class SignUpRequest {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotNull(message = "이메일은 비어있을 수 없습니다.")
    private String email;
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String username;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password1;
    @NotBlank(message = "비밀번호확인은 비어있을 수 없습니다.")
    private String password2;
    @NotNull(message = "동아리는 비어있을 수 없습니다.")
    private Integer club;

    @Builder
    private SignUpRequest(String email, String username, String password1, String password2, Integer club) {
        this.email = email;
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.club = club;
    }
    
    public SignUp toSignUp(PasswordEncoder encoder) {
        return SignUp.builder()
                .email(email)
                .username(username)
                .password(encoder.encode(password1))
                .club(Club.fromInt(club))
                .build();
    }
}
