package Dompoo.Hongpoong.request.auth;

import Dompoo.Hongpoong.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Member.Club club;

    @Builder
    public SignUpRequest(String email, String username, String password1, String password2, Member.Club club) {
        this.email = email;
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
        this.club = club;
    }
}
