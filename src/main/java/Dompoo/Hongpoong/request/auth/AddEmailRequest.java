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
public class AddEmailRequest {

    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @Builder
    public AddEmailRequest(String email) {
        this.email = email;
    }
}
