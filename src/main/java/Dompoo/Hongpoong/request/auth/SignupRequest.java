package Dompoo.Hongpoong.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String username;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password1;
    @NotBlank(message = "비밀번호확인은 비어있을 수 없습니다.")
    private String password2;

    @Builder
    public SignupRequest(String email, String username, String password1, String password2) {
        this.email = email;
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
    }
}
