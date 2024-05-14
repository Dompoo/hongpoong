package Dompoo.Hongpoong.request.auth;

import jakarta.validation.constraints.NotBlank;
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
    "email": "dompoo@gmail.com"
}
 */
public class EmailValidRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Builder
    public EmailValidRequest(String email) {
        this.email = email;
    }
}
