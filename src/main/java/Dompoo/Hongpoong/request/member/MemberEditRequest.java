package Dompoo.Hongpoong.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberEditRequest {

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String username;
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password1;
    @NotBlank(message = "비밀번호확인은 비어있을 수 없습니다.")
    private String password2;

    @Builder
    public MemberEditRequest(String username, String password1, String password2) {
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
    }
}
